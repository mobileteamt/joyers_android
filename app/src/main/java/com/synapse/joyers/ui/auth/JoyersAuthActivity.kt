package com.synapse.joyers.ui.auth

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.synapse.joyers.R
import com.synapse.joyers.databinding.ActivityJoyersAuthBinding

class JoyersAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoyersAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@JoyersAuthActivity,
            R.layout.activity_joyers_auth
        )


        val auth1 = Html.fromHtml(getString(R.string.joyer_auth_1), Html.FROM_HTML_MODE_LEGACY)
        val auth2 = Html.fromHtml(getString(R.string.joyer_auth_2), Html.FROM_HTML_MODE_LEGACY)
        val auth3 = Html.fromHtml(getString(R.string.joyer_auth_3), Html.FROM_HTML_MODE_LEGACY)
        val auth4 = Html.fromHtml(getString(R.string.joyer_auth_4), Html.FROM_HTML_MODE_LEGACY)

        binding.title1.text = auth1
        binding.title2.text = auth2
        binding.title3.text = auth3
        binding.title4.text = auth4
    }
}