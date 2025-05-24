package com.synapse.joyers.apiData.request

data class VerifyEmailRequest(
    val email: String? = null,
    val mobile: String? = null
)