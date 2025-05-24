package com.synapse.joyers.ui.signup

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.synapse.joyers.apiData.Repository
import com.synapse.joyers.apiData.request.ConfirmVerificationRequest
import com.synapse.joyers.apiData.request.RegisterRequest
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.request.UsernameRequest
import com.synapse.joyers.apiData.request.VerifyEmailRequest
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.apiData.response.TitlesApiResponse
import com.synapse.joyers.apiData.response.UploadResponse
import com.synapse.joyers.apiData.response.UserResponse
import com.synapse.joyers.apiData.response.UsernameCheckResponse
import com.synapse.joyers.apiData.response.VerifyEmailResponse
import com.synapse.joyers.utils.NetWorkResult
import com.synapse.joyers.utils.uriToFile
import com.synapse.joyers.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class SignupViewModel(private val repository: Repository, application: Application) :
    BaseViewModel(application) {

    private val _nameCheckResponse: MutableLiveData<NetWorkResult<UsernameCheckResponse>> =
        MutableLiveData()
    val nameCheckResponse: LiveData<NetWorkResult<UsernameCheckResponse>> = _nameCheckResponse

    private val _emailVerifyResponse: MutableLiveData<NetWorkResult<VerifyEmailResponse>> =
        MutableLiveData()
    val emailVerifyResponse: LiveData<NetWorkResult<VerifyEmailResponse>> = _emailVerifyResponse

    private val _confirmVerificationResponse: MutableLiveData<NetWorkResult<BaseResponse>> =
        MutableLiveData()
    val confirmVerificationResponse: LiveData<NetWorkResult<BaseResponse>> =
        _confirmVerificationResponse

    private val _registerResponse: MutableLiveData<NetWorkResult<LoginResponse>> =
        MutableLiveData()
    val registerResponse: LiveData<NetWorkResult<LoginResponse>> = _registerResponse

    private val _titlesApiResponse: MutableLiveData<NetWorkResult<TitlesApiResponse>> =
        MutableLiveData()
    val titlesApiResponse: LiveData<NetWorkResult<TitlesApiResponse>> = _titlesApiResponse

    private val _imageUploadResponse: MutableLiveData<NetWorkResult<UploadResponse>> =
        MutableLiveData()
    val imageUploadResponse: LiveData<NetWorkResult<UploadResponse>> = _imageUploadResponse

    private val _userInfoResponse: MutableLiveData<NetWorkResult<BaseResponse>> =
        MutableLiveData()
    val userInfoResponse: LiveData<NetWorkResult<BaseResponse>> = _userInfoResponse

    private val _setPageResponse: MutableLiveData<NetWorkResult<BaseResponse>> =
        MutableLiveData()
    val setPageResponse: LiveData<NetWorkResult<BaseResponse>> = _setPageResponse


    private val _checkPageResponse: MutableLiveData<NetWorkResult<UserResponse>> =
        MutableLiveData()
    val checkPageResponse: LiveData<NetWorkResult<UserResponse>> = _checkPageResponse

    fun checkUserName(request: UsernameRequest) = viewModelScope.launch {
        repository.checkUsername(context, request).collect { values ->
            _nameCheckResponse.value = values
        }
    }

    fun checkUserEmail(request: VerifyEmailRequest) = viewModelScope.launch {
        repository.checkEmail(context, request).collect { values ->
            _emailVerifyResponse.value = values
        }
    }

    fun confirmVerification(request: ConfirmVerificationRequest) = viewModelScope.launch {
        repository.confirmVerification(context, request).collect { values ->
            _confirmVerificationResponse.value = values
        }
    }

    fun signup(request: RegisterRequest) = viewModelScope.launch {
        repository.register(context, request).collect { values ->
            _registerResponse.value = values
        }
    }

    fun getTitles(token: String) = viewModelScope.launch {
        repository.getTitles(context, token).collect { values ->
            _titlesApiResponse.value = values
        }
    }

    fun uploadImage(imageUri: Uri, token: String) = viewModelScope.launch {
        val file = uriToFile(context, imageUri)
        val mimeType = when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        repository.uploadImage(context, body, token).collect { values ->
            _imageUploadResponse.value = values
        }
    }


    fun updateUser(token: String, userId: String, updateRequest: UpdateUserRequest) =
        viewModelScope.launch {
            repository.updateUser(context, token, userId, updateRequest).collect { values ->
                _userInfoResponse.value = values
            }
        }

    fun checkPageNumber(token: String, userId: String) =
        viewModelScope.launch {
            repository.checkPageNumber(context, token, userId).collect { values ->
                _checkPageResponse.value = values
            }
        }

    fun setPageNumber(token: String, userId: String, updateRequest: UpdateUserRequest) =
        viewModelScope.launch {
            repository.setPageNumber(context, token, userId, updateRequest).collect { values ->
                _setPageResponse.value = values
            }
        }


}