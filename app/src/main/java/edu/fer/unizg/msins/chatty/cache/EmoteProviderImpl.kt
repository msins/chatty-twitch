package edu.fer.unizg.msins.chatty.cache

import android.graphics.Bitmap
import edu.fer.unizg.msins.chatty.emotes.Emote
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

//rewrite this
class EmoteProviderImpl(private val channel: String) : EmoteProvider {
    private val emotes = ConcurrentHashMap<String, Bitmap>()
    private val gifs = ConcurrentHashMap<String, ByteBuffer>()

    override fun saveEmote(emote: Emote) {
        emotes[getKey(emote.descriptor.name)] = emote.bitmap
    }

    override fun getEmote(name: String): Bitmap? {
        var emote = emotes[getKey(name)]
        if (emote != null) {
            return emote
        }
        emote = EmoteProvider.global().getEmote(GlobalEmoteProvider.getKey(name))
        if (emote != null) {
            return emote
        }
        return null
    }

    private fun getKey(emoteName: String): String {
        return "${EmoteProvider.CACHE_BASE}.$channel.$emoteName"
    }

    override fun getGif(name: String): ByteBuffer? {
        return gifs[getKey(name)] ?: EmoteProvider.global().getGif(GlobalEmoteProvider.getKey(name))
    }

    override fun saveGif(byteBuffer: ByteBuffer, emote: EmoteDescriptor) {
        gifs[getKey(emote.name)] = byteBuffer
    }
}