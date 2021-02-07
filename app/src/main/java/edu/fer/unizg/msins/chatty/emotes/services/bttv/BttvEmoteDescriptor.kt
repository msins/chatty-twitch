package edu.fer.unizg.msins.chatty.emotes.services.bttv

import com.squareup.moshi.Json
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import edu.fer.unizg.msins.chatty.networking.API

data class BttvEmoteDescriptor(
    override val id: String,
    @Json(name = "code")
    override val name: String,
    override val width: Int = 0,
    override val height: Int = 0,
    @Json(name = "imageType")
    override val type: EmoteDescriptor.Type
) : EmoteDescriptor {
    override fun createUrl(): String = API.BTTV_CDN_BASE_URL + id + "/3x"
}