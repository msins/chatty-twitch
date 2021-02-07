package edu.fer.unizg.msins.chatty.networking

import edu.fer.unizg.msins.chatty.cache.EmoteProvider
import edu.fer.unizg.msins.chatty.emotes.Emote
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import edu.fer.unizg.msins.chatty.networking.EmoteDownloadTask
import java.nio.ByteBuffer

class GlobalEmoteDownloadTask(
    descriptor: EmoteDescriptor,
    channel: String
) : EmoteDownloadTask(descriptor, channel) {
    override fun onGifEmoteDownloaded(buffer: ByteBuffer) {
        EmoteProvider.global().saveGif(buffer, descriptor)
    }

    override fun onStaticEmoteDownloaded(emote: Emote) {
        EmoteProvider.global().saveEmote(emote)
    }
}