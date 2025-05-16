package com.synapse.joyers.ui.signup

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivitySignupBinding
import androidx.core.graphics.toColorInt

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && s.toString().length > 3) {
                    binding.gifLoader.visibility = View.VISIBLE
                    /* api hit */
                    if (s.toString() == "bini") {
                        binding.gifLoader.visibility = View.GONE
                        binding.usernameError.visibility = View.VISIBLE
                        binding.usernameError.text =
                            "This username is already taken. Please try another."
                        binding.closeNReload.visibility = View.VISIBLE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_red_border)
                    } else {
                        binding.gifLoader.visibility = View.GONE
                        binding.linearGreenTick.visibility = View.VISIBLE
                        binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
                        binding.usernameError.visibility = View.GONE
                        binding.closeNReload.visibility = View.GONE

                    }
                } else {
                    /* button disable */
                    binding.gifLoader.visibility = View.GONE
                    binding.linearGreenTick.visibility = View.GONE
                    binding.linearUsername.setBackgroundResource(R.drawable.light_grey_background)
                    binding.usernameError.visibility = View.GONE
                    binding.closeNReload.visibility = View.GONE
                }
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


        }

        binding.imgPhone.setOnClickListener {
            binding.linearPhone.visibility = View.VISIBLE
            binding.linearEmail.visibility = View.GONE
            binding.emailError.visibility = View.GONE

        }

        binding.reloadUsername.setOnClickListener {
            /* hit api */
        }

        binding.editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    binding.btnSignup.text = getString(R.string.next)
                    binding.emailError.visibility = View.GONE
                    binding.linearEmailError.setBackgroundResource(R.drawable.light_grey_background)

                } else {
                    binding.emailError.visibility = View.VISIBLE
                    binding.emailError.text = getString(R.string.invalidEmail)
                    binding.linearEmailError.setBackgroundResource(R.drawable.light_grey_red_border)

                }
            }
        })

        binding.btnSignup.setOnClickListener {
            if (binding.btnSignup.text == getString(R.string.next)) {
                binding.verifyLinear.visibility = View.VISIBLE
                binding.btnSignup.visibility = View.GONE
            } else {
                binding.verifyLinear.visibility = View.GONE
                val intent = Intent(this, IdentityActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnVerify.setOnClickListener {
            binding.verifyLinear.visibility = View.GONE
            binding.linearPassword.visibility = View.VISIBLE
            binding.linearConfirmPassword.visibility = View.VISIBLE
            binding.btnSignup.visibility = View.VISIBLE
            binding.btnSignup.text = getString(R.string.sign_up)
        }

        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.progressLinear.visibility = View.VISIBLE
                    evaluatePasswordStrength(s.toString(), binding.textStrength)
                } else {
                    binding.progressLinear.visibility = View.GONE

                }

            }
        })


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
                textStrength.gravity = Gravity.START
            }  // Weak
            3, 4 -> {
                color = "#FFA700"
                textStrength.text = getString(R.string.good)
                textStrength.gravity = Gravity.CENTER

            }   // Good
            else -> {
                color = "#0ED916"
                textStrength.text = getString(R.string.strong)
                textStrength.gravity = Gravity.END

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
}