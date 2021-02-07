package edu.fer.unizg.msins.chatty.networking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import edu.fer.unizg.msins.chatty.cache.EmoteProvider
import edu.fer.unizg.msins.chatty.emotes.Emote
import edu.fer.unizg.msins.chatty.emotes.EmoteDescriptor
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels

//TODO replace with coroutines not threads
abstract class EmoteDownloadTask(val descriptor: EmoteDescriptor, val channel: String) : Runnable {
    override fun run() {
        val url = descriptor.createUrl()

        try {
            when (descriptor.type) {
                EmoteDescriptor.Type.GIF -> {
                    val gifBuffer = downloadGifEmote(url)
                    onGifEmoteDownloaded(gifBuffer)
                }
                EmoteDescriptor.Type.STATIC -> {
                    val emote = downloadStaticEmote(url)
                    onStaticEmoteDownloaded(emote)
                }
            }
        } catch (e: Exception) {
            Timber.wtf(e)
        }
    }

    abstract fun onGifEmoteDownloaded(buffer: ByteBuffer)
    abstract fun onStaticEmoteDownloaded(emote: Emote)

    private fun downloadStaticEmote(url: String): Emote {
        return URL(url).openStream().use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
            val adjustedWidth = (112 / bitmap.height.toDouble() * bitmap.width).toInt()
            Emote(
                descriptor,
                Bitmap.createScaledBitmap(bitmap, adjustedWidth, 112, true)
            )
        }
    }

    private fun downloadGifEmote(url: String): ByteBuffer {
        val urlConnection = URL(url).openConnection()
        urlConnection.connect()
        val contentLength = urlConnection.contentLength
        if (contentLength < 0) {
            throw IOException("Content-Length header not present")
        }
        urlConnection.getInputStream().use {
            val buffer = ByteBuffer.allocateDirect(contentLength)
            Channels.newChannel(it).use { c ->
                while (buffer.remaining() > 0) {
                    c.read(buffer)
                }
            }
            return buffer
        }
    }
}

