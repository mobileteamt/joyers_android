package com.synapse.joyers.ui.signup

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.request.ConfirmVerificationRequest
import com.synapse.joyers.apiData.request.RegisterRequest
import com.synapse.joyers.apiData.request.UsernameRequest
import com.synapse.joyers.apiData.request.VerifyEmailRequest
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.apiData.response.UsernameCheckResponse
import com.synapse.joyers.apiData.response.VerifyEmailResponse
import com.synapse.joyers.databinding.ActivitySignupBinding
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.suggestiondialog.SuggestionAdapter
import com.synapse.joyers.utils.showProgress
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by inject()
    private val preferencesManager: PreferencesManager by inject()
    private var searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    var isPasswordVisible = false
    var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        observeApiPostData()

        addVerificationCodeWatcher()

        binding.frameLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.frameLayout.getWindowVisibleDisplayFrame(rect)

            val screenHeight = binding.frameLayout.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is visible
                val widthDp = 125
                val heightDp = 50
                val scale = resources.displayMetrics.density
                val widthPx = (widthDp * scale).toInt()
                val heightPx = (heightDp * scale).toInt()

                //set the view height
                val layoutParams = binding.view.layoutParams
                val scale1 = resources.displayMetrics.density
                val height = (20 * scale1).toInt()
                layoutParams.height = height // height in pixels
                binding.view.layoutParams = layoutParams


                // Set the size
                val params = binding.logo.layoutParams
                params.width = widthPx
                params.height = heightPx
                binding.logo.layoutParams = params
            } else {
                // Keyboard is hidden
                // Convert dp to pixels
                val widthDp = 165
                val heightDp = 70
                val scale = resources.displayMetrics.density
                val widthPx = (widthDp * scale).toInt()
                val heightPx = (heightDp * scale).toInt()

                //set the view height
                val layoutParams = binding.view.layoutParams
                val scale1 = resources.displayMetrics.density
                val height = (55 * scale1).toInt()
                layoutParams.height = height // height in pixels
                binding.view.layoutParams = layoutParams

                // Set the size
                val params = binding.logo.layoutParams
                params.width = widthPx
                params.height = heightPx
                binding.logo.layoutParams = params

            }
        }

        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    if (s.toString().isNotEmpty()) {
                        binding.gifLoader.visibility = View.VISIBLE
                        signupViewModel.checkUserName(UsernameRequest(s.toString()))
                    } else {
                        /* button disable */
                        binding.gifLoader.visibility = View.GONE
                        binding.linearGreenTick.visibility = View.GONE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
                        binding.usernameError.visibility = View.GONE
                        binding.closeNReload.visibility = View.GONE
                        binding.recyclerSuggestions.visibility = View.GONE
                    }
                }
                searchHandler.postDelayed(searchRunnable!!, 500) // 500ms debounce
            }
        })


        binding.closeUsername.setOnClickListener {
            binding.username.setText("")
            binding.gifLoader.visibility = View.GONE
            binding.closeNReload.visibility = View.GONE
            binding.usernameError.visibility = View.GONE
            binding.linearGreenTick.visibility = View.GONE
            binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)

        }

        binding.imgUser.setOnClickListener {
            binding.linearPhone.visibility = View.GONE
            binding.linearEmail.visibility = View.VISIBLE
            binding.phoneNumberError.visibility = View.GONE
            validateAndLogin()

        }

        binding.imgPhone.setOnClickListener {
            binding.linearPhone.visibility = View.VISIBLE
            binding.linearEmail.visibility = View.GONE
            binding.emailError.visibility = View.GONE
            validateAndLogin()
        }

        binding.login.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.clearEmail.visibility = View.VISIBLE
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                        binding.emailError.visibility = View.GONE
                        binding.linearEmailError.setBackgroundResource(R.drawable.light_grey_background)
                    } else {
                        binding.emailError.visibility = View.VISIBLE
                        binding.emailError.text = getString(R.string.invalidEmail)
                        binding.linearEmailError.setBackgroundResource(R.drawable.light_grey_red_border)

                    }
                } else {
                    binding.clearEmail.visibility = View.GONE
                    binding.linearEmail.setBackgroundResource(R.drawable.light_grey_background)

                }

                validateAndLogin()

            }
        })

        binding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateAndLogin()
                if (s.toString().isNotEmpty()) {
                    binding.clearPhone.visibility = View.VISIBLE
                } else {
                    binding.clearPhone.visibility = View.GONE

                }
            }
        })


        binding.closeUsername.setOnClickListener {
            binding.username.setText("")
            binding.gifLoader.visibility = View.GONE
            binding.linearGreenTick.visibility = View.GONE
            binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
            binding.usernameError.visibility = View.GONE
            binding.closeNReload.visibility = View.GONE
            binding.recyclerSuggestions.visibility = View.GONE

        }

        binding.clearEmail.setOnClickListener {
            binding.editEmail.setText("")
            binding.linearEmail.setBackgroundResource(R.drawable.light_grey_background)

        }

        binding.clearPhone.setOnClickListener {
            binding.editPhone.setText("")
            binding.linearPhone.setBackgroundResource(R.drawable.light_grey_background)

        }

        binding.reloadUsername.setOnClickListener {
            binding.gifLoader.visibility = View.VISIBLE
            signupViewModel.checkUserName(UsernameRequest(binding.username.text.toString()))
        }

        binding.btnSignup.setOnClickListener {
            if (binding.btnSignup.text == getString(R.string.next)) {
                if (binding.linearEmail.isVisible) {
                    binding.codeSendText.text = getString(R.string.code_sent_to_email)
                    signupViewModel.checkUserEmail(VerifyEmailRequest(email = binding.editEmail.text.toString()))
                } else {
                    binding.codeSendText.text = getString(R.string.code_sent_to_phone)
                    signupViewModel.checkUserEmail(VerifyEmailRequest(mobile = binding.editPhone.text.toString()))
                }
            } else {
                signupViewModel.signup(
                    RegisterRequest(
                        email = binding.editEmail.text.toString(),
                        username = binding.username.text.toString(),
                        password = binding.editPassword.text.toString(),
                        confirmPassword = binding.editConfirmPassword.text.toString()
                    )
                )
            }
        }

        binding.btnResendCode.setOnClickListener {
            if (binding.linearEmail.isVisible) {
                binding.codeSendText.text = getString(R.string.code_sent_to_email)
                signupViewModel.checkUserEmail(VerifyEmailRequest(email = binding.editEmail.text.toString()))
            } else {
                binding.codeSendText.text = getString(R.string.code_sent_to_phone)
                signupViewModel.checkUserEmail(VerifyEmailRequest(mobile = binding.editPhone.text.toString()))
            }
        }

        binding.btnVerify.setOnClickListener {
            if (binding.linearEmail.isVisible) {
                signupViewModel.confirmVerification(
                    ConfirmVerificationRequest(
                        email = binding.editEmail.text.toString(),
                        code = binding.verificationCode.text.toString()
                    )
                )
            } else {
                signupViewModel.confirmVerification(
                    ConfirmVerificationRequest(
                        mobile = binding.editPhone.text.toString(),
                        code = binding.verificationCode.text.toString()
                    )
                )
            }
        }

        binding.clearVerification.setOnClickListener {
            binding.verificationCode.setText("")
        }

        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.passwordToggle.visibility = View.VISIBLE
                    binding.progressLinear.visibility = View.VISIBLE
                    evaluatePasswordStrength(s.toString(), binding.textStrength)
                } else {
                    binding.passwordToggle.visibility = View.GONE
                    binding.progressLinear.visibility = View.GONE

                }

                if(binding.editConfirmPassword.text.toString().isNotEmpty()) {
                    if (s.toString() == binding.editConfirmPassword.text.toString()
                    ) {
                        binding.btnSignup.text = resources.getString(R.string.sign_up)
                        binding.btnSignup.isEnabled = true
                        binding.btnSignup.setTextColor(resources.getColor(R.color.white))
                    } else {
                        binding.btnSignup.text = resources.getString(R.string.sign_up)
                        binding.btnSignup.isEnabled = false
                        binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))
                    }
                }

            }
        })

        binding.editConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.confirmPasswordToggle.visibility = View.VISIBLE
                } else {
                    binding.confirmPasswordToggle.visibility = View.GONE
                }

                if (s.toString() == binding.editPassword.text.toString()
                ) {
                    binding.btnSignup.text = resources.getString(R.string.sign_up)
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.setTextColor(resources.getColor(R.color.white))
                } else {
                    binding.btnSignup.text = resources.getString(R.string.sign_up)
                    binding.btnSignup.isEnabled = false
                    binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))
                }

            }
        })

        binding.passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.editPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.show_password) // show eye
            } else {
                binding.editPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.password_hide) // hide eye
            }
            // Maintain cursor position
            binding.editPassword.setSelection(binding.editPassword.text!!.length)
        }

        binding.confirmPasswordToggle.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                binding.editConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.show_password) // show eye
            } else {
                binding.editConfirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordToggle.setImageResource(R.drawable.password_hide) // hide eye
            }
            // Maintain cursor position
            binding.editConfirmPassword.setSelection(binding.editConfirmPassword.text!!.length)
        }

    }

    fun evaluatePasswordStrength(password: String, textStrength: AppCompatTextView) {
        var score = 0

        if (password.length >= 8) score++                // Length check
        if (password.any { it.isLowerCase() }) score++   // Lowercase
        if (password.any { it.isUpperCase() }) score++   // Uppercase
        if (password.any { it.isDigit() }) score++       // Digit
        if (password.any { !it.isLetterOrDigit() }) score++ // Special char

        val percentage = (score * 20) // Max 100

        val color: String
        when (score) {
            in 0..2 -> {
                color = "#FF0000"
                textStrength.text = getString(R.string.weak)
                textStrength.setTextColor(color.toColorInt())
                textStrength.gravity = Gravity.START
            }  // Weak
            3, 4 -> {
                color = "#FFA700"
                textStrength.text = getString(R.string.good)
                textStrength.gravity = Gravity.CENTER
                textStrength.setTextColor(color.toColorInt())


            }   // Good
            else -> {
                color = "#0ED916"
                textStrength.text = getString(R.string.strong)
                textStrength.gravity = Gravity.END
                textStrength.setTextColor(color.toColorInt())

            }  // Strong
        }

        binding.strengthProgressBar.progress = percentage

        val drawable = binding.strengthProgressBar.progressDrawable.mutate()
        val clipDrawable = (drawable as LayerDrawable).findDrawableByLayerId(android.R.id.progress)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            clipDrawable.colorFilter = BlendModeColorFilter(color.toColorInt(), BlendMode.SRC_IN)
        } else {
            @Suppress("DEPRECATION")
            clipDrawable.setColorFilter(color.toColorInt(), PorterDuff.Mode.SRC_IN)
        }

        binding.strengthProgressBar.progressDrawable = drawable
    }

    private fun observeApiPostData() {
        signupViewModel.nameCheckResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<UsernameCheckResponse>(
                this,
                onLoading = { showProgress(binding.progressBar.circularProgress, false) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    val suggestionList = it?.data?.suggestions ?: emptyList()

                    val adapter = SuggestionAdapter(
                        items = suggestionList,
                        parseItem = { it }) { selected ->
                        binding.username.setText(selected)
                        binding.gifLoader.visibility = View.GONE
                        binding.linearGreenTick.visibility = View.VISIBLE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
                        binding.usernameError.visibility = View.GONE
                        binding.closeNReload.visibility = View.GONE
                        binding.recyclerSuggestions.visibility = View.GONE
                    }
                    binding.recyclerSuggestions.layoutManager = LinearLayoutManager(this)
                    binding.recyclerSuggestions.adapter = adapter
                    if (suggestionList.isEmpty()) {
                        binding.gifLoader.visibility = View.GONE
                        binding.linearGreenTick.visibility = View.VISIBLE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
                        binding.usernameError.visibility = View.GONE
                        binding.closeNReload.visibility = View.GONE
                        binding.recyclerSuggestions.visibility = View.GONE
                        validateAndLogin()
                    } else {
                        binding.gifLoader.visibility = View.GONE
                        binding.usernameError.visibility = View.VISIBLE
                        binding.usernameError.text =
                            getString(R.string.username_is_already_taken)
                        binding.closeNReload.visibility = View.VISIBLE
                        binding.linearGreenTick.visibility = View.GONE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_red_border)
                        binding.recyclerSuggestions.visibility = View.VISIBLE

                    }
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        signupViewModel.emailVerifyResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<VerifyEmailResponse>(
                this,
                onLoading = { showProgress(binding.progressBar.circularProgress, false) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    if (it?.success!!) {
                        binding.verifyLinear.visibility = View.VISIBLE
                        binding.btnSignup.visibility = View.GONE

                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                    }
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        signupViewModel.confirmVerificationResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<BaseResponse>(
                this,
                onLoading = { showProgress(binding.progressBar.circularProgress, false) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    if (it?.success!!) {
                        binding.verifyLinear.visibility = View.GONE
                        binding.linearPassword.visibility = View.VISIBLE
                        binding.linearConfirmPassword.visibility = View.VISIBLE
                        binding.btnSignup.visibility = View.VISIBLE
                        binding.btnSignup.text = resources.getString(R.string.sign_up)
                        binding.btnSignup.isEnabled = false
                        binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))

                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        signupViewModel.registerResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<LoginResponse>(
                this,
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    if (it?.success!!) {
                        lifecycleScope.launch {
                            it.data?.token?.let { it1 -> preferencesManager.saveAccessToken(it1) }
                            it.data?.user?.id?.let { it1 -> preferencesManager.saveUserId(it1) }
                        }

                        val intent = Intent(this, IdentityActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }

    private fun validateAndLogin() {
        val usernameEmpty = binding.username.text.isNullOrBlank()
        val phoneEmpty = binding.editPhone.text.isNullOrBlank()
        val emailEmpty = binding.editEmail.text.isNullOrBlank()
        val validEmail =
            android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editEmail.text.toString()).matches()

        if (binding.linearEmail.isVisible) {
            if (validEmail) {
                if (usernameEmpty || emailEmpty) {
                    binding.btnSignup.text = resources.getString(R.string.sign_up)
                    binding.btnSignup.isEnabled = false
                    binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))
                } else {
                    binding.btnSignup.text = resources.getString(R.string.next)
                    binding.btnSignup.isEnabled = true
                    binding.btnSignup.setTextColor(resources.getColor(R.color.white))
                }
            } else {
                binding.btnSignup.text = resources.getString(R.string.sign_up)
                binding.btnSignup.isEnabled = false
                binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))
            }
        } else {
            if (phoneEmpty || usernameEmpty) {
                binding.btnSignup.text = resources.getString(R.string.sign_up)
                binding.btnSignup.isEnabled = false
                binding.btnSignup.setTextColor(resources.getColor(R.color.dim_white))
            } else {
                binding.btnSignup.text = resources.getString(R.string.next)
                binding.btnSignup.isEnabled = true
                binding.btnSignup.setTextColor(resources.getColor(R.color.white))
            }
        }


    }

    private fun addVerificationCodeWatcher() = with(binding.verificationCode) {
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrEmpty()) {
                    val semiBoldTypeface =
                        ResourcesCompat.getFont(this@SignupActivity, R.font.lato_semi_bold)
                    letterSpacing = 0.2f
                    typeface = semiBoldTypeface
                    binding.btnVerify.isEnabled = true
                    binding.btnVerify.setTextColor(resources.getColor(R.color.white))
                    binding.clearVerification.visibility = View.VISIBLE
                } else {
                    letterSpacing = 0f
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    binding.btnVerify.isEnabled = false
                    binding.btnVerify.setTextColor(resources.getColor(R.color.dim_white))
                    binding.clearVerification.visibility = View.INVISIBLE

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}