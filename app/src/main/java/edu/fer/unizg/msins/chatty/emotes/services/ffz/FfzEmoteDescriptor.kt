package edu.fer.unizg.msins.chatty.emotes.services.ffz

import com.squareup.moshi.Json
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor

class FfzEmoteDescriptor(
    override val height: Int,
    override val width: Int,
    override val id: String,
    override val name: String,
    override val type: EmoteDescriptor.Type = EmoteDescriptor.Type.STATIC,
    private val urls: Urls
) : EmoteDescriptor {

    override fun createUrl(): String {
        return "https:" + (urls.big ?: (urls.medium ?: urls.small!!))
    }

    data class Urls(
        @Json(name = "1") val small: String?,
        @Json(name = "2") val medium: String?,
        @Json(name = "4") val big: String?
    )
}




