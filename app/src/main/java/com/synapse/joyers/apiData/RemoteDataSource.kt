package com.synapse.joyers.apiData

import com.synapse.joyers.apiData.request.ConfirmVerificationRequest
import com.synapse.joyers.apiData.request.ForgotPasswordRequest
import com.synapse.joyers.apiData.request.LoginRequest
import com.synapse.joyers.apiData.request.RegisterRequest
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.request.UsernameRequest
import com.synapse.joyers.apiData.request.VerifyEmailRequest
import com.synapse.joyers.apiData.request.VerifyResetCodeRequest
import okhttp3.MultipartBody


class RemoteDataSource(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest) = apiService.login(request)

    suspend fun forgotPassword(request: ForgotPasswordRequest) = apiService.forgotPassword(request)

    suspend fun verifyResetCode(request: VerifyResetCodeRequest) =
        apiService.verifyResetCode(request)

    suspend fun checkUsername(request: UsernameRequest) = apiService.checkUsername(request)

    suspend fun verifyEmail(request: VerifyEmailRequest) = apiService.verifyEmail(request)

    suspend fun confirmVerification(request: ConfirmVerificationRequest) =
        apiService.confirmVerification(request)

    suspend fun register(request: RegisterRequest) = apiService.register(request)

    suspend fun getPopupTitles(token: String) =
        apiService.getTitles(authToken = "Bearer $token", page = 1, limit = 50)

    suspend fun uploadImage(image: MultipartBody.Part, token: String) =
        apiService.uploadImage(authToken = "Bearer $token", file = image)

    suspend fun updateUser(userId: String, token: String, userRequest: UpdateUserRequest) =
        apiService.updateUser(authToken = "Bearer $token", userId = userId, request = userRequest)

    suspend fun checkPageNumber(userId: String, token: String) =
        apiService.checkPageNumber(authToken = "Bearer $token", userId = userId)

    suspend fun setPageNumber(userId: String, token: String, userRequest: UpdateUserRequest) =
        apiService.setPageNumber(authToken = "Bearer $token", userId = userId, updateRequest = userRequest)


}