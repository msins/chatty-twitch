package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class FollowDto(
    @Json(name = "from_id") val fromId: String,
    @Json(name = "from_name") val fromName: String,
    @Json(name = "to_id") val toId: String,
    @Json(name = "to_name") val toName: String,
    @Json(name = "followed_at") val followed: String
)