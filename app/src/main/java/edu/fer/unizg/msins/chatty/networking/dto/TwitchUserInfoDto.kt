package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class TwitchUserInfoDto(
    @Json(name = "id") val id: String,
    @Json(name = "login") val name: String,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "type") val type: String,
    @Json(name = "broadcaster_type") val broadcasterType: String,
    @Json(name = "description") val description: String,
    @Json(name = "profile_image_url") val avatarUrl: String,
    @Json(name = "view_count") val viewCount: Int
)