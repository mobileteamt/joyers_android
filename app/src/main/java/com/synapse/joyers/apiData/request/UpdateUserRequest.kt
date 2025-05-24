package com.synapse.joyers.apiData.request

data class UpdateUserRequest(
    val firstname: String? = null,
    val lastname: String? = null,
    val nickname: String? = null,
    val mobile: String? = null,
    val joyerStatus: String? = null,
    val is_identity_verified: Boolean? = null,
    val is_status_verified: Boolean? = null,
    val is_oath_verified: Boolean? = null,
    val is_skipped: Boolean? = null,
    val JoyTitleId: String? = null,
    val profilePictureId: String? = null,
    val backgroundPictureId: String? = null,
    val location: String? = null
)
