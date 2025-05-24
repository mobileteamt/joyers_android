package com.synapse.joyers.ui.loginforgot

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.request.LoginRequest
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.databinding.ActivityLoginBinding
import com.synapse.joyers.ui.signup.SignupActivity
import com.synapse.joyers.utils.showBottomSocialDialog
import com.synapse.joyers.utils.showProgress
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    private lateinit var binding: ActivityLoginBinding
    var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setupViewToggle()
        setupClickListeners()
        setupTextWatchers()
        observeApiPostData()

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
                val height = (45 * scale1).toInt()
                layoutParams.height = height // height in pixels
                binding.view.layoutParams = layoutParams

                // Set the size
                val params = binding.logo.layoutParams
                params.width = widthPx
                params.height = heightPx
                binding.logo.layoutParams = params
            }
        }
    }

    private fun setupViewToggle() {
        binding.imgUser.setOnClickListener {
            toggleView(isPhone = false)
            clearFieldError(binding.linearPhoneError, binding.phoneNumberError)
            validateAndLogin()
        }

        binding.imgPhone.setOnClickListener {
            toggleView(isPhone = true)
            clearFieldError(binding.linearUsernameError, binding.usernameError)
            validateAndLogin()
        }
    }

    private fun setupClickListeners() {
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.clearUsername.setOnClickListener {
            binding.editUsername.setText("")
            setViewVisibility(binding.clearUsername, false)
            validateAndLogin()
        }

        binding.clearPhone.setOnClickListener {
            binding.editPhone.setText("")
            setViewVisibility(binding.clearPhone, false)
            validateAndLogin()
        }


        binding.btnLogin.setOnClickListener {
            var identifier: String = ""
            val isValidInput: Boolean

            if (binding.linearUsername.isVisible) {
                val input = binding.editUsername.text.toString().trim()

                if (input.contains("@")) {
                    if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                        identifier = input
                        isValidInput = true
                    } else {
                        showFieldError(
                            binding.linearUsernameError,
                            binding.usernameError,
                            getString(R.string.invaild_email)
                        )
                        isValidInput = false
                    }
                } else {
                    identifier = input
                    isValidInput = true
                }
            } else {
                val input = binding.editPhone.text.toString().trim()

                if (input.length != 10) {
                    showFieldError(
                        binding.linearPhoneError,
                        binding.phoneNumberError,
                        getString(R.string.invaild_phone)
                    )
                    isValidInput = false
                    identifier = "" // placeholder
                } else {
                    identifier = input
                    isValidInput = true
                }
            }

            if (isValidInput) {
                val loginRequest = LoginRequest(
                    identifier = identifier,
                    password = binding.editPassword.text.toString().trim(),
                    rememberMe = true
                )
                loginViewModel.login(loginRequest)
            }
        }


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

        binding.linearJoinWith.setOnClickListener {
            showBottomSocialDialog(
                this,
                onFacebookClick = {
                    Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show()
                },
                onGoogleClick = {
                    Toast.makeText(this, "Google", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupTextWatchers() {
        binding.editPassword.addTextChangedListener(afterTextChanged = {
            validateAndLogin()
            setViewVisibility(binding.passwordToggle, it?.isNotEmpty() == true)
        })

        binding.editUsername.addTextChangedListener(afterTextChanged = {
            validateAndLogin()
            setViewVisibility(binding.clearUsername, it?.isNotEmpty() == true)
        })

        binding.editPhone.addTextChangedListener(afterTextChanged = {
            validateAndLogin()
            setViewVisibility(binding.clearPhone, it?.isNotEmpty() == true)
        })
    }

    private fun validateAndLogin() {
        val usernameEmpty = binding.editUsername.text.isNullOrBlank()
        val phoneEmpty = binding.editPhone.text.isNullOrBlank()
        val passwordEmpty = binding.editPassword.text.isNullOrBlank()

        if (binding.linearUsername.isVisible) {
            if (usernameEmpty || passwordEmpty) {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.setTextColor(getResources().getColor(R.color.dim_white))
            } else {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.setTextColor(getResources().getColor(R.color.white))
            }
        } else {
            if (phoneEmpty || passwordEmpty) {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.setTextColor(getResources().getColor(R.color.dim_white))
            } else {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.setTextColor(getResources().getColor(R.color.white))
            }
        }


    }

    private fun toggleView(isPhone: Boolean) {
        binding.linearPhone.visibility = if (isPhone) View.VISIBLE else View.GONE
        binding.linearUsername.visibility = if (isPhone) View.GONE else View.VISIBLE
    }

    private fun showFieldError(layout: View, errorView: View, message: String) {
        layout.setBackgroundResource(R.drawable.light_grey_red_border)
        if (errorView is androidx.appcompat.widget.AppCompatTextView) {
            errorView.text = message
        }
        errorView.visibility = View.VISIBLE
    }

    private fun clearFieldError(layout: View, errorView: View) {
        layout.setBackgroundResource(R.drawable.light_grey_background)
        errorView.visibility = View.GONE
    }

    private fun setViewVisibility(view: View?, isVisible: Boolean) {
        view?.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun observeApiPostData() {
        loginViewModel.loginResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<LoginResponse>(
                this,
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }


    // Extension function for simpler TextWatcher
    private fun Editable?.isNotEmpty(): Boolean = this != null && this.toString().isNotEmpty()

    private inline fun android.widget.EditText.addTextChangedListener(
        crossinline afterTextChanged: (Editable?) -> Unit
    ) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = afterTextChanged(s)
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
