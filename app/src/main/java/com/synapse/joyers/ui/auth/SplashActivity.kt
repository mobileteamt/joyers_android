package com.synapse.joyers.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.response.UserResponse
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.loginforgot.LoginActivity
import com.synapse.joyers.ui.signup.IdentityActivity
import com.synapse.joyers.ui.signup.SignupViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SplashActivity : AppCompatActivity() {

    private val preferencesManager: PreferencesManager by inject()
    private val signupViewModel: SignupViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        observeApiPostData()

        lifecycleScope.launch {
            val token = preferencesManager.getAccessToken()
            val userID = preferencesManager.getUserId()
            if (token != null && userID != null) {
                signupViewModel.checkPageNumber(token, userID)
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }


    private fun observeApiPostData() {
        signupViewModel.checkPageResponse.observe(this) { response ->
            val apiResultHandler = ApiResultHandler<UserResponse>(
                this,
                onLoading = { /*showProgress(binding.progressBar.circularProgress, true) */ },
                onSuccess = {
                    when {
                        it?.is_oath_verified == true -> {
                            //navController.navigate("Dashboard")
                        }

                        it?.is_skipped == true -> {
                            val intent = Intent(this, SplashVideoActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        it?.is_status_verified == true -> {
                            val intent = IdentityActivity.newIntent(this, 2)
                            startActivity(intent)
                            finish()
                        }

                        it?.is_identity_verified == true -> {
                            val intent = IdentityActivity.newIntent(this, 1)
                            startActivity(intent)
                            finish()
                        }

                        else -> {
                            val intent = IdentityActivity.newIntent(this, 0)
                            startActivity(intent)
                            finish()
                        }
                    }
                },
                onFailure = {
                    // showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }


    }
}
