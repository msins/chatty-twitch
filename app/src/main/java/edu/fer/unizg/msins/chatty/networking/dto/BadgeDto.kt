package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class BadgesSetDto(
    @Json(name = "badge_sets") val sets: Map<String, BadgesDto>
)

data class BadgesDto(
    @Json(name = "versions") val ver: Map<String, BadgeDto>
)

data class BadgeDto(
    @Json(name = "image_url_4x") val imageUrl: String
)