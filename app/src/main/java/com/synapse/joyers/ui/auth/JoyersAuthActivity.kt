package com.synapse.joyers.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.databinding.ActivityJoyersAuthBinding
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.loginforgot.LoginActivity
import com.synapse.joyers.ui.signup.SignupViewModel
import com.synapse.joyers.utils.showProgress
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class JoyersAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoyersAuthBinding
    private val preferencesManager: PreferencesManager by inject()
    private val signupViewModel: SignupViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@JoyersAuthActivity,
            R.layout.activity_joyers_auth
        )
        observeApiPostData()

        val auth1 = Html.fromHtml(getString(R.string.joyer_auth_1), Html.FROM_HTML_MODE_LEGACY)
        val auth2 = Html.fromHtml(getString(R.string.joyer_auth_2), Html.FROM_HTML_MODE_LEGACY)
        val auth3 = Html.fromHtml(getString(R.string.joyer_auth_3), Html.FROM_HTML_MODE_LEGACY)
        val auth4 = Html.fromHtml(getString(R.string.joyer_auth_4), Html.FROM_HTML_MODE_LEGACY)

        binding.title1.text = auth1
        binding.title2.text = auth2
        binding.title3.text = auth3
        binding.title4.text = auth4

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                val token = preferencesManager.getAccessToken()
                val userID = preferencesManager.getUserId()
                if (token != null && userID != null) {
                    signupViewModel.setPageNumber(
                        token = token, userId = userID,
                        updateRequest = UpdateUserRequest(is_oath_verified = true)
                    )
                }
            }
        }
    }

    private fun observeApiPostData() {
        signupViewModel.setPageResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<BaseResponse>(
                this,
                onLoading = { /*showProgress(binding.progressBar.circularProgress, true)*/ },
                onSuccess = {
                    /*  showProgress(binding.progressBar.circularProgress, false)*/

                },
                onFailure = {
                    /*showProgress(binding.progressBar.circularProgress, false)*/
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }
}