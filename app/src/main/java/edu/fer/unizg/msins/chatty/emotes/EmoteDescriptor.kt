package edu.fer.unizg.msins.chatty.emotes

import com.squareup.moshi.Json

interface EmoteDescriptor {
    val id: String
    val name: String
    val type: Type
    val width: Int
    val height: Int

    fun createUrl(): String

    enum class Type {
        @Json(name = "png")
        STATIC,
        @Json(name = "gif")
        GIF
    }
}