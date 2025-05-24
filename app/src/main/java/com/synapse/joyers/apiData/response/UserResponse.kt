package com.synapse.joyers.apiData.response

data class UserResponse(
    val joyerStatus: String? = null,
    val emailVerified: Boolean? = null,
    val mobileVerified: Boolean? = null,
    val bio: String? = null,
    val role: String? = null,
    val roles: List<String>? = null,
    val is_identity_verified: Boolean? = null,
    val is_active: Boolean? = null,
    val is_deleted: Boolean? = null,
    val is_skipped: Boolean? = null,
    val is_status_verified: Boolean? = null,
    val is_oath_verified: Boolean? = null,
    val location: String? = null,
    val _id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val createdAt: String? = null,
    val dateOfBirth: String? = null,
    val firstname: String? = null
) {



}
