package com.synapse.joyers.apiData.response

data class VerifyEmailResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: VerifyEmailData? = null
)

data class VerifyEmailData(
    val email: String? = null
)
