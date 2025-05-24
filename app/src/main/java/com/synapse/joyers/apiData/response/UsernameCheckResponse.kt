package com.synapse.joyers.apiData.response

data class UsernameCheckResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: UsernameData? = null
)

data class UsernameData(
    val available: Boolean? = null,
    val username: String? = null,
    val suggestions: List<String>? = null
)
