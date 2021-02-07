package edu.fer.unizg.msins.chatty.networking.dto

import com.squareup.moshi.Json

data class TwitchEmotesDto(
    @Json(name = "emotes") var emotes: List<TwitchEmoteDto>
)

data class TwitchEmoteDto(
    @Json(name = "code") var name: String,
    @Json(name = "id") var id: Int
)