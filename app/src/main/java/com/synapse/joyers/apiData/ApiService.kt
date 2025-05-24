package com.synapse.joyers.apiData

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
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("backend/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("backend/api/password/forgot")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    @POST("backend/api/password/verify-reset")
    suspend fun verifyResetCode(
        @Body request: VerifyResetCodeRequest
    ): Response<VerifyResetCodeResponse>

    @POST("backend/api/auth/check-username")
    suspend fun checkUsername(@Body request: UsernameRequest): Response<UsernameCheckResponse>

    @POST("backend/api/auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<VerifyEmailResponse>

    @POST("backend/api/auth/confirm-verification")
    suspend fun confirmVerification(@Body request: ConfirmVerificationRequest): Response<BaseResponse>

    @POST("backend/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("backend/api/titles")
    suspend fun getTitles(
        @Header("Authorization") authToken: String,
        @Header("client-type") clientType: String = "mobile",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<TitlesApiResponse>


    @Multipart
    @POST("backend/api/media/upload")
    suspend fun uploadImage(
        @Header("Authorization") authToken: String,
        @Header("client-type") clientType: String = "mobile", @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    @PUT("backend/api/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") authToken: String,
        @Header("client-type") clientType: String = "mobile",
        @Path("id") userId: String,
        @Body request: UpdateUserRequest
    ): Response<BaseResponse>

    @GET("backend/api/users/{id}")
    suspend fun checkPageNumber(
        @Header("Authorization") authToken: String,
        @Header("client-type") clientType: String = "mobile",
        @Path("id") userId: String
    ): Response<UserResponse>

    @PUT("backend/api/users/{id}")
    suspend fun setPageNumber(
        @Header("Authorization") authToken: String,
        @Header("client-type") clientType: String = "mobile",
        @Path("id") userId: String,
        @Body updateRequest: UpdateUserRequest
    ): Response<BaseResponse>
}