package com.synapse.joyers.apiData.request

data class LoginRequest(
    val identifier: String,
    val password: String,
    val rememberMe: Boolean
)