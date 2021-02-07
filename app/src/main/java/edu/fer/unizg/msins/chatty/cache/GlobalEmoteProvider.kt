package edu.fer.unizg.msins.chatty.cache

import android.graphics.Bitmap
import edu.fer.unizg.msins.chatty.emotes.Emote
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

class GlobalEmoteProvider : EmoteProvider {
    private val emotes = ConcurrentHashMap<String, Bitmap>()
    private val gifs = ConcurrentHashMap<String, ByteBuffer>()

    companion object{
        fun getKey(name: String): String {
            return "${EmoteProvider.GLOBAL_CACHE}.$name"
        }
    }

    override fun getEmote(name: String): Bitmap? {
        return emotes[getKey(name)]
    }

    override fun saveEmote(emote: Emote) {
        emotes[getKey(emote.descriptor.name)] = emote.bitmap
    }

    override fun getGif(name: String): ByteBuffer? {
        return gifs[getKey(name)]
    }

    override fun saveGif(byteBuffer: ByteBuffer, emote: EmoteDescriptor) {
        gifs[getKey(emote.name)] = byteBuffer
    }
}