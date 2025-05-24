package com.synapse.joyers.apiData

import android.content.Context
import com.synapse.joyers.apiData.request.ConfirmVerificationRequest
import com.synapse.joyers.apiData.request.ForgotPasswordRequest
import com.synapse.joyers.apiData.request.LoginRequest
import com.synapse.joyers.apiData.request.RegisterRequest
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.request.UsernameRequest
import com.synapse.joyers.apiData.request.VerifyEmailRequest
import com.synapse.joyers.apiData.response.VerifyEmailResponse
import com.synapse.joyers.apiData.request.VerifyResetCodeRequest
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.ForgotPasswordResponse
import com.synapse.joyers.apiData.response.LoginResponse
import com.synapse.joyers.apiData.response.TitlesApiResponse
import com.synapse.joyers.apiData.response.UploadResponse
import com.synapse.joyers.apiData.response.UserResponse
import com.synapse.joyers.apiData.response.UsernameCheckResponse
import com.synapse.joyers.apiData.response.VerifyResetCodeResponse
import com.synapse.joyers.utils.NetWorkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import org.json.JSONObject

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun login(context: Context, request: LoginRequest): Flow<NetWorkResult<LoginResponse>> {
        return toResultFlow(context) {
            remoteDataSource.login(request)
        }
    }

    suspend fun forgotPassword(
        context: Context,
        request: ForgotPasswordRequest
    ): Flow<NetWorkResult<ForgotPasswordResponse>> {
        return toResultFlow(context) {
            remoteDataSource.forgotPassword(request)
        }
    }

    suspend fun verifyResetCode(
        context: Context,
        request: VerifyResetCodeRequest
    ): Flow<NetWorkResult<VerifyResetCodeResponse>> {
        return toResultFlow(context) {
            remoteDataSource.verifyResetCode(request)
        }
    }

    suspend fun checkUsername(
        context: Context,
        request: UsernameRequest
    ): Flow<NetWorkResult<UsernameCheckResponse>> {
        return toResultFlow(context) {
            remoteDataSource.checkUsername(request)
        }
    }

    suspend fun checkEmail(
        context: Context,
        request: VerifyEmailRequest
    ): Flow<NetWorkResult<VerifyEmailResponse>> {
        return toResultFlow(context) {
            remoteDataSource.verifyEmail(request)
        }
    }

    suspend fun confirmVerification(
        context: Context,
        request: ConfirmVerificationRequest
    ): Flow<NetWorkResult<BaseResponse>> {
        return toResultFlow(context) {
            remoteDataSource.confirmVerification(request)
        }
    }

    suspend fun register(
        context: Context,
        request: RegisterRequest
    ): Flow<NetWorkResult<LoginResponse>> {
        return toResultFlow(context) {
            remoteDataSource.register(request)
        }
    }

    suspend fun getTitles(context: Context, token: String): Flow<NetWorkResult<TitlesApiResponse>> {
        return toResultFlow(context) {
            remoteDataSource.getPopupTitles(token)
        }
    }

    suspend fun uploadImage(
        context: Context,
        part: MultipartBody.Part,
        token: String
    ): Flow<NetWorkResult<UploadResponse>> {
        return toResultFlow(context) {
            remoteDataSource.uploadImage(part, token)
        }
    }

    suspend fun updateUser(
        context: Context,
        token: String,
        userId: String,
        updateUserRequest: UpdateUserRequest
    ): Flow<NetWorkResult<BaseResponse>> {
        return toResultFlow(context) {
            remoteDataSource.updateUser(userId, token, updateUserRequest)
        }
    }

    suspend fun checkPageNumber(
        context: Context,
        token: String,
        userId: String,
    ): Flow<NetWorkResult<UserResponse>> {
        return toResultFlow(context) {
            remoteDataSource.checkPageNumber(userId = userId, token = token)
        }
    }

    suspend fun setPageNumber(
        context: Context,
        token: String,
        userId: String,
        updateUserRequest: UpdateUserRequest
    ): Flow<NetWorkResult<BaseResponse>> {
        return toResultFlow(context) {
            remoteDataSource.setPageNumber(
                userId = userId,
                token = token,
                userRequest = updateUserRequest
            )
        }
    }


}