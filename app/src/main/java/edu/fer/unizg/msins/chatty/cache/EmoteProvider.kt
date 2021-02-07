package edu.fer.unizg.msins.chatty.cache

import android.graphics.Bitmap
import edu.fer.unizg.msins.chatty.emotes.Emote
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import edu.fer.unizg.msins.chatty.preferences.PrefsTag
import java.nio.ByteBuffer

//rewrite this
interface EmoteProvider {

    companion object {
        const val CACHE_BASE = PrefsTag.KEY_EMOTE_CACHE
        const val GLOBAL_CACHE = PrefsTag.GLOBAL_KEY_EMOTE_CACHE

        private var INSTANCE: EmoteProviderImpl? = null
        private var GLOBAL_INSTANCE: GlobalEmoteProvider? = null

        fun global(): GlobalEmoteProvider {
            if (GLOBAL_INSTANCE == null) {
                synchronized(this) {
                    if (GLOBAL_INSTANCE == null) {
                        GLOBAL_INSTANCE = GlobalEmoteProvider()
                    }
                    return GLOBAL_INSTANCE!!
                }
            }

            return GLOBAL_INSTANCE!!
        }

        fun forChannel(channel: String): EmoteProviderImpl {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = EmoteProviderImpl(channel)
                    }
                    return INSTANCE!!
                }
            }

            return INSTANCE!!
        }
    }

    fun getGif(name: String): ByteBuffer?
    fun saveGif(byteBuffer: ByteBuffer, emote: EmoteDescriptor)
    fun getEmote(name: String): Bitmap?
    fun saveEmote(emote: Emote)
}