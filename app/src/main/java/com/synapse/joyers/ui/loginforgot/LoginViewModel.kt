package com.synapse.joyers.ui.loginforgot

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.synapse.joyers.apiData.Repository
import com.synapse.joyers.apiData.request.ForgotPasswordRequest
import com.synapse.joyers.apiData.request.LoginRequest
import com.synapse.joyers.apiData.request.VerifyResetCodeRequest
import com.synapse.joyers.apiData.response.ForgotPasswordResponse
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.apiData.response.VerifyResetCodeResponse
import com.synapse.joyers.utils.NetWorkResult
import com.synapse.joyers.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository, application: Application): BaseViewModel(application) {

    private val _loginResponse: MutableLiveData<NetWorkResult<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<NetWorkResult<LoginResponse>> = _loginResponse

    private val _forgotResponse: MutableLiveData<NetWorkResult<ForgotPasswordResponse>> = MutableLiveData()
    val forgotResponse: LiveData<NetWorkResult<ForgotPasswordResponse>> = _forgotResponse

    private val _verifyResponse: MutableLiveData<NetWorkResult<VerifyResetCodeResponse>> = MutableLiveData()
    val verifyResponse: LiveData<NetWorkResult<VerifyResetCodeResponse>> = _verifyResponse


    fun login(request: LoginRequest) = viewModelScope.launch {
        repository.login(context, request).collect { values ->
            _loginResponse.value = values
        }
    }


    fun forgotPassword(request: ForgotPasswordRequest) = viewModelScope.launch {
        repository.forgotPassword(context, request).collect { values ->
            _forgotResponse.value = values
        }
    }

    fun verifyResetCode(request: VerifyResetCodeRequest) = viewModelScope.launch {
        repository.verifyResetCode(context, request).collect { values ->
            _verifyResponse.value = values
        }
    }


}