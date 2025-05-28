package com.synapse.joyers.models

data class FeedItem(
    val title: String,
    val type: String,
    val rating: Int,
    val timestamp: String,
    val location: String,
    val description: String,
    val imageResId: Int
)
