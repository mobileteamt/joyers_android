package com.synapse.joyers.apiData.response

data class LoginResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val data: LoginData? = null
)

data class LoginData(
    val success: Boolean? = null,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: String? = null,
    val username: String? = null,
    val status: String? = null,
    val dateOfBirth: String? = null,
    val email: String? = null,
    val role: String? = null,
    val location: String? = null,
    val is_oath_verified: Boolean? = null,
    val is_skipped: Boolean? = null,
    val is_identity_verified: Boolean? = null,
    val is_status_verified: Boolean? = null,
    val profilePicture: String? = null,
    val backgroundPicture: String? = null,
    val stats: Stats? = null
)

data class Stats(
    val followers: Int? = null,
    val following: Int? = null
)
