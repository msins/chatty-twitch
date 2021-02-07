package edu.fer.unizg.msins.chatty.cache

import android.graphics.Bitmap
import java.util.concurrent.ConcurrentHashMap

object BadgeProvider {
    private val badges = HashMap<String, Bitmap>()
    private val versions = HashMap<String, List<Int>>()

    fun getBadge(channel: String, month: Int): Bitmap? {
        if (versions[channel] == null) return null
        return badges[getKey(channel, month)]
    }

    fun saveBadge(badge: Bitmap, channel: String, month: Int) {
        badges[getKey(channel, month)] = badge
    }

    private fun getKey(channel: String, month: Int): String {
        val version = versions[channel]!!.closestValue(month)
        return "$channel.$version"
    }

    fun addVersions(channel: String, subStreaks: List<Int>) {
        versions[channel] = subStreaks
    }
}

fun List<Int>.closestValue(value: Int) = minBy { kotlin.math.abs(value - it) }