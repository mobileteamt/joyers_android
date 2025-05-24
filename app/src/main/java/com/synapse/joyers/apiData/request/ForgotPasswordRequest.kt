package com.synapse.joyers.apiData.request

data class ForgotPasswordRequest(
    val email: String,
    val mobile: String
)