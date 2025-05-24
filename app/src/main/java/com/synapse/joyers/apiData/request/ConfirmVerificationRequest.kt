package com.synapse.joyers.apiData.request

data class ConfirmVerificationRequest(
    val email: String? = null,
    val mobile: String? = null,
    val code: String? = null
)