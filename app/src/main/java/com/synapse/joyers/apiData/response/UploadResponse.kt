package com.synapse.joyers.apiData.response

data class UploadResponse(
    val success: Boolean,
    val message: String,
    val data: UploadedFileData
)

data class UploadedFileData(
    val _id: String,
    val userId: String,
    val originalName: String,
    val filename: String,
    val path: String,
    val size: Long,
    val type: String,
    val mimeType: String,
    val createdAt: String,
    val __v: Int
)
