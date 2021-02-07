package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "login") val login: String,
    @Json(name = "user_id") val userId: Int
)