package com.synapse.joyers.ui.loginforgot

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivityLoginBinding
import com.synapse.joyers.ui.signup.SignupActivity
import com.synapse.joyers.utils.showBottomSocialDialog
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginBinding.imgUser.setOnClickListener {
            loginBinding.linearPhone.visibility = View.GONE
            loginBinding.linearUsername.visibility = View.VISIBLE

        }

        loginBinding.imgPhone.setOnClickListener {
            loginBinding.linearPhone.visibility = View.VISIBLE
            loginBinding.linearUsername.visibility = View.GONE
        }

        loginBinding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        loginBinding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    loginBinding.passwordToggle.visibility = View.VISIBLE
                } else {
                    loginBinding.passwordToggle.visibility = View.GONE
                }
            }
        })

        loginBinding.editUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    loginBinding.clearUsername.visibility = View.VISIBLE
                } else {
                    loginBinding.clearUsername.visibility = View.INVISIBLE
                }
            }
        })

        loginBinding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    loginBinding.clearPhone.visibility = View.VISIBLE
                } else {
                    loginBinding.clearPhone.visibility = View.INVISIBLE
                }
            }
        })

        loginBinding.clearUsername.setOnClickListener {
            loginBinding.editUsername.setText("")
            loginBinding.clearUsername.visibility = View.INVISIBLE


        }

        loginBinding.clearPhone.setOnClickListener {
            loginBinding.editPhone.setText("")
            loginBinding.clearPhone.visibility = View.INVISIBLE

        }

        loginBinding.btnLogin.setOnClickListener {
            if (loginBinding.editUsername.text.isNullOrBlank()) {
                loginBinding.linearUsernameError.setBackgroundResource(R.drawable.light_grey_red_border)
                loginBinding.usernameError.text = "Username is required"
                loginBinding.usernameError.visibility = View.VISIBLE
                loginBinding.phoneNumberError.visibility = View.GONE

            } else {
                loginBinding.usernameError.setBackgroundResource(R.drawable.light_grey_background)
                loginBinding.usernameError.visibility = View.GONE
                loginBinding.phoneNumberError.visibility = View.GONE

            }

            if (loginBinding.editPhone.text.isNullOrBlank()) {
                loginBinding.linearPhoneError.setBackgroundResource(R.drawable.light_grey_red_border)
                loginBinding.phoneNumberError.text = "Phonenumber is required"
                loginBinding.phoneNumberError.visibility = View.VISIBLE
                loginBinding.usernameError.visibility = View.GONE

            } else {
                loginBinding.phoneNumberError.setBackgroundResource(R.drawable.light_grey_background)
                loginBinding.phoneNumberError.visibility = View.GONE
                loginBinding.usernameError.visibility = View.GONE

            }

            if (loginBinding.editPassword.text.isNullOrBlank()) {
                loginBinding.linearPassword.setBackgroundResource(R.drawable.red_all_curved)
                loginBinding.passwordError.text = "Password is required"
                loginBinding.passwordError.visibility = View.VISIBLE
            } else {
                loginBinding.passwordError.setBackgroundResource(R.drawable.light_grey_background)
                loginBinding.passwordError.visibility = View.GONE
            }
        }

        loginBinding.linearJoinWith.setOnClickListener {
            showBottomSocialDialog(
                this@LoginActivity,
                onFacebookClick = {
                    Toast.makeText(this@LoginActivity, "Facebook", Toast.LENGTH_SHORT).show()
                }, onGoogleClick = {
                    Toast.makeText(this@LoginActivity, "Google", Toast.LENGTH_SHORT).show()
                }

            )
        }
    }
}