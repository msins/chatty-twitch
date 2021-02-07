package edu.fer.unizg.msins.chatty.emotes.services.twitchemotes

import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import edu.fer.unizg.msins.chatty.networking.API

class TwitchEmoteDescriptor(
    override val id: String,
    override val name: String,
    override val type: EmoteDescriptor.Type,
    override val width: Int = 0,
    override val height: Int = 0
) : EmoteDescriptor {
    override fun createUrl(): String = "${API.TWITCH_EMOTES_CDN_BASE_URL}$id/3.0"
}