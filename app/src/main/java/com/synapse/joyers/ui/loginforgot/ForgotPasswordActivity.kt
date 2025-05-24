package com.synapse.joyers.ui.loginforgot

import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.request.ForgotPasswordRequest
import com.synapse.joyers.apiData.request.LoginRequest
import com.synapse.joyers.apiData.request.VerifyResetCodeRequest
import com.synapse.joyers.apiData.response.ForgotPasswordResponse
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.apiData.response.VerifyResetCodeResponse
import com.synapse.joyers.databinding.ActivityForgotPasswordBinding
import com.synapse.joyers.utils.maskEmail
import com.synapse.joyers.utils.maskPhone
import com.synapse.joyers.utils.setNormalBold
import org.koin.android.ext.android.inject

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

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
                val height = (30 * scale1).toInt()
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

        observeApiPostData()
        setupTextWatchers()
        setupUI()

    }

    private fun setupUI() = with(binding) {
        disableTabs()

        imgUser.setOnClickListener {
            toggleInputMode(isPhone = false)
            validateButton()
            clearFieldError(binding.linearPhoneError, binding.phoneNumberError)

        }
        imgPhone.setOnClickListener {
            toggleInputMode(isPhone = true)
            validateButton()
            clearFieldError(binding.linearUsernameError, binding.usernameError)

        }

        btnNext.setOnClickListener { handleNextClick() }
        btnLogin.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        btnVerify.setOnClickListener {
            if (binding.linearUsername.isVisible) {
                val input = binding.editUsername.text.toString().trim()
                val code = binding.verificationCode.text.toString().trim()
                loginViewModel.verifyResetCode(VerifyResetCodeRequest(email = input, code = code))

            } else {
                val input = binding.editPhone.text.toString().trim()
                val code = binding.verificationCode.text.toString().trim()
                loginViewModel.verifyResetCode(VerifyResetCodeRequest(mobile = input, code = code))

            }
        }
        btnResendCode.setOnClickListener { handleNextClick() }

        clearUsername.setOnClickListener { clearEditText(editUsername, clearUsername) }
        clearPhone.setOnClickListener { clearEditText(editPhone, clearPhone) }
        clearVerification.setOnClickListener { clearEditText(verificationCode, clearVerification) }

        addClearButtonListener(editUsername, clearUsername)
        addClearButtonListener(editPhone, clearPhone)
        addClearButtonListener(verificationCode, clearVerification)
        addVerificationCodeWatcher()

    }

    private fun disableTabs() {
        listOf(0, 1).forEach {
            binding.tabLayout.getTabAt(it)?.view?.apply {
                if (it == 0) {
                    isSelected = true
                    isEnabled = false
                } else {
                    isSelected = false
                    isEnabled = false
                }
            }
        }
    }

    private fun toggleInputMode(isPhone: Boolean) = with(binding) {
        val usernameMode = !isPhone

        linearUsername.visibility = if (usernameMode) View.VISIBLE else View.GONE
        linearPhone.visibility = if (usernameMode) View.GONE else View.VISIBLE

        tabLayout.getTabAt(if (usernameMode) 0 else 1)?.select()

        linearTabVerification.visibility = View.VISIBLE
        linearVerify.visibility = View.GONE
        linearPassword.visibility = View.GONE
        btnNext.visibility = View.VISIBLE
        resetPasswordText.text = getString(R.string.reset_password_in_two_steps)

        clearErrorStyles()
    }

    private fun handleNextClick() = with(binding) {
        var identifier: String = ""
        val isValidInput: Boolean

        if (binding.linearUsername.isVisible) {
            val input = binding.editUsername.text.toString().trim()

            if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                identifier = input
                isValidInput = true
                clearErrorStyles()
            } else {
                validateInput(
                    linearUsernameError,
                    usernameError,
                    false,
                )
                isValidInput = false
            }

        } else {
            val input = binding.editPhone.text.toString().trim()

            if (input.length != 10) {
                validateInput(linearPhoneError, phoneNumberError, true)
                isValidInput = false
                identifier = "" // placeholder
            } else {
                identifier = input
                isValidInput = true
                clearErrorStyles()
            }
        }

        if (isValidInput) {
            val forgotPasswordRequest = if (binding.linearUsername.isVisible) {
                ForgotPasswordRequest(
                    email = identifier.trim(),
                    mobile = "",
                )
            } else {
                ForgotPasswordRequest(
                    email = "",
                    mobile = identifier.trim(),
                )
            }
            loginViewModel.forgotPassword(forgotPasswordRequest)
        }


    }

    private fun validateInput(
        errorContainer: View,
        errorTextView: androidx.appcompat.widget.AppCompatTextView,
        isPhone: Boolean
    ) {
        showError(
            errorContainer,
            errorTextView,
            if (isPhone) resources.getString(R.string.invaild_phone) else resources.getString(R.string.invaild_email)
        )

    }

    private fun showError(container: View, errorTextView: View, message: String) {
        container.setBackgroundResource(R.drawable.light_grey_red_border)
        if (errorTextView is androidx.appcompat.widget.AppCompatTextView) {
            errorTextView.text = message
        }
        errorTextView.visibility = View.VISIBLE
    }


    private fun clearFieldError(layout: View, errorView: View) {
        layout.setBackgroundResource(R.drawable.light_grey_background)
        errorView.visibility = View.GONE
    }

    private fun clearErrorStyles() = with(binding) {
        linearUsernameError.setBackgroundResource(R.drawable.light_grey_background)
        linearPhoneError.setBackgroundResource(R.drawable.light_grey_background)
        usernameError.text = ""
        phoneNumberError.text = ""
    }

    private fun proceedToVerification(identifier: String, isPhone: Boolean) = with(binding) {
        linearTabVerification.visibility = View.GONE
        btnNext.visibility = View.GONE
        linearVerify.visibility = View.VISIBLE
        linearPassword.visibility = View.VISIBLE

        val message =
            if (isPhone) getString(R.string.number_sent) else getString(R.string.email_sent)

        val txt =
            if (isPhone) maskPhone(identifier) else maskEmail(identifier)

        resetPasswordText.text = setNormalBold(txt, message)
    }

    private fun clearEditText(editText: View, clearButton: View) {
        (editText as? androidx.appcompat.widget.AppCompatEditText)?.setText("")
        clearButton.visibility = View.INVISIBLE
    }

    private fun addClearButtonListener(
        editText: androidx.appcompat.widget.AppCompatEditText,
        clearButton: View
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = if (s.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun addVerificationCodeWatcher() = with(binding.verificationCode) {
        addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!text.isNullOrEmpty()) {
                    letterSpacing = 0.2f
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
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

    private fun setupTextWatchers() {
        binding.editUsername.addTextChangedListener(afterTextChanged = {
            validateButton()
        })

        binding.editPhone.addTextChangedListener(afterTextChanged = {
            validateButton()
        })
    }

    private fun validateButton() {
        val usernameEmpty = binding.editUsername.text.isNullOrBlank()
        val phoneEmpty = binding.editPhone.text.isNullOrBlank()

        if (binding.linearUsername.isVisible) {
            if (usernameEmpty) {
                binding.btnNext.isEnabled = false
                binding.btnNext.setTextColor(getResources().getColor(R.color.dim_white))
            } else {
                binding.btnNext.isEnabled = true
                binding.btnNext.setTextColor(getResources().getColor(R.color.white))
            }
        } else {
            if (phoneEmpty) {
                binding.btnNext.isEnabled = false
                binding.btnNext.setTextColor(getResources().getColor(R.color.dim_white))
            } else {
                binding.btnNext.isEnabled = true
                binding.btnNext.setTextColor(getResources().getColor(R.color.white))
            }
        }
    }

    private inline fun android.widget.EditText.addTextChangedListener(
        crossinline afterTextChanged: (Editable?) -> Unit
    ) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = afterTextChanged(s)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeApiPostData() {
        loginViewModel.forgotResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<ForgotPasswordResponse>(
                this,
                onLoading = { showProgress(true) },
                onSuccess = {
                    showProgress(false)
                    Toast.makeText(this, it?.message.toString(), Toast.LENGTH_SHORT).show()
                    if (binding.linearUsername.isVisible) {
                        val input = binding.editUsername.text.toString().trim()
                        proceedToVerification(input, false)
                    } else {
                        val input = binding.editPhone.text.toString().trim()
                        proceedToVerification(input, true)

                    }


                },
                onFailure = {
                    showProgress(false)
                    Toast.makeText(this, response?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        loginViewModel.verifyResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<VerifyResetCodeResponse>(
                this,
                onLoading = { showProgress(true) },
                onSuccess = {
                    showProgress(false)
                    Toast.makeText(this, it?.message.toString(), Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    showProgress(false)
                    Toast.makeText(this, response?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }

    private fun showProgress(isShown: Boolean) {
        binding.progressBar.circularProgress.visibility = if (isShown) View.VISIBLE else View.GONE
    }


}
