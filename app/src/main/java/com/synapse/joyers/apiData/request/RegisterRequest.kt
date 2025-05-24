package com.synapse.joyers.apiData.request

data class RegisterRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null
)
