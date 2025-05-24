package com.synapse.joyers.apiData.request

data class VerifyResetCodeRequest(
    val email: String? = null,
    val mobile: String? = null,
    val code: String? = null
)