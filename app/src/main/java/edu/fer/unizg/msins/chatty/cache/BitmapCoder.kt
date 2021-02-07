package edu.fer.unizg.msins.chatty.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object BitmapCoder {
    fun encodeToBase64(image: Bitmap): String {
        val byteOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteOutputStream)
        val bytes = byteOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decodeFromBase64(encoded: String): Bitmap {
        val bytes = Base64.decode(encoded, 0)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}