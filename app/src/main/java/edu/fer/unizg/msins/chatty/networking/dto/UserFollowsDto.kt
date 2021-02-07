package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class UserFollowsDto(
    @Json(name = "total") val total: Int,
    @Json(name = "data") val data: List<FollowDto>
)