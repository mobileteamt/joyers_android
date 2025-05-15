package com.synapse.joyers.ui.loginforgot

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivityForgotPasswordBinding
import com.synapse.joyers.utils.setNormalBold
import org.koin.android.ext.android.inject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        val tab1 = binding.tabLayout.getTabAt(0)
        val tab2 = binding.tabLayout.getTabAt(1)
        tab1?.view?.isSelected = false
        tab1?.view?.isEnabled = false
        tab2?.view?.isSelected = false
        tab2?.view?.isEnabled = false

        binding.imgUser.setOnClickListener {
            binding.linearPhone.visibility = View.GONE
            binding.linearUsername.visibility = View.VISIBLE
            val tab = binding.tabLayout.getTabAt(0)
            tab?.select()
            binding.phoneNumberError.setBackgroundResource(R.drawable.light_grey_background)
            binding.btnNext.visibility = View.VISIBLE
            binding.resetPasswordText.text = getString(R.string.reset_password_in_two_steps)
            binding.linearTabVerification.visibility = View.VISIBLE
            binding.linearVerify.visibility = View.GONE
            binding.linearPassword.visibility = View.GONE

        }

        binding.imgPhone.setOnClickListener {
            binding.linearPhone.visibility = View.VISIBLE
            binding.linearUsername.visibility = View.GONE
            val tab = binding.tabLayout.getTabAt(1)
            tab?.select()
            binding.usernameError.setBackgroundResource(R.drawable.light_grey_background)
            binding.btnNext.visibility = View.VISIBLE
            binding.resetPasswordText.text = getString(R.string.reset_password_in_two_steps)
            binding.linearTabVerification.visibility = View.VISIBLE
            binding.linearVerify.visibility = View.GONE
            binding.linearPassword.visibility = View.GONE
        }

        binding.verificationCode.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0?.length!! > 0) {
                        binding.verificationCode.letterSpacing = 0.2f
                        binding.verificationCode.typeface =
                            Typeface.defaultFromStyle(Typeface.BOLD)
                    } else {
                        binding.verificationCode.letterSpacing = 0.0f
                        binding.verificationCode.typeface =
                            Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

        binding.btnNext.setOnClickListener {
            if (binding.linearUsername.isVisible) {
                if (binding.editUsername.text.isNullOrBlank()) {
                    binding.linearUsernameError.setBackgroundResource(R.drawable.light_grey_red_border)
                    binding.usernameError.text = "Username is required"
                    binding.resetPasswordText.text = getString(R.string.reset_password_in_two_steps)
                } else {
                    binding.linearUsernameError.setBackgroundResource(R.drawable.light_grey_background)
                    binding.usernameError.setBackgroundResource(R.drawable.light_grey_background)
                    binding.phoneNumberError.setBackgroundResource(R.drawable.light_grey_background)

                    binding.linearTabVerification.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                    binding.linearVerify.visibility = View.VISIBLE
                    binding.linearPassword.visibility = View.VISIBLE
                    binding.resetPasswordText.text = setNormalBold(
                        binding.editUsername.text.toString(),
                        resources.getString(R.string.email_sent)
                    )
                }
            } else {
                if (binding.editPhone.text.isNullOrBlank()) {
                    binding.linearPhoneError.setBackgroundResource(R.drawable.light_grey_red_border)
                    binding.phoneNumberError.text = "Phonenumber is required"
                    binding.resetPasswordText.text = getString(R.string.reset_password_in_two_steps)

                } else {
                    binding.linearPhoneError.setBackgroundResource(R.drawable.light_grey_background)
                    binding.phoneNumberError.setBackgroundResource(R.drawable.light_grey_background)
                    binding.usernameError.setBackgroundResource(R.drawable.light_grey_background)
                    binding.linearTabVerification.visibility = View.GONE

                    binding.btnNext.visibility = View.GONE
                    binding.linearVerify.visibility = View.VISIBLE
                    binding.linearPassword.visibility = View.VISIBLE
                    binding.resetPasswordText.text = setNormalBold(
                        binding.editPhone.text.toString(),
                        resources.getString(R.string.number_sent)
                    )
                }
            }

        }

        binding.editUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.clearUsername.visibility = View.VISIBLE
                } else {
                    binding.clearUsername.visibility = View.INVISIBLE
                }
            }
        })

        binding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.clearPhone.visibility = View.VISIBLE
                } else {
                    binding.clearPhone.visibility = View.INVISIBLE
                }
            }
        })

        binding.clearUsername.setOnClickListener {
            binding.editUsername.setText("")
            binding.clearUsername.visibility = View.INVISIBLE
        }

        binding.btnLogin.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}