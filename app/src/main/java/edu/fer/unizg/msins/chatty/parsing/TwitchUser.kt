package edu.fer.unizg.msins.chatty.parsing

import android.graphics.Color
import org.pircbotx.User
import timber.log.Timber

class TwitchUser(
    val user: User,
    private val tags: Map<String, String>
) {
    companion object {
        const val COLOR = "color"
        const val DISPLAY_NAME = "display-name"
        const val SUBSCRIBER = "subscriber"
        const val TURBO = "turbo"
        const val MOD = "mod"
        const val EMOTES = "emotes"
        const val BADGE_INFO = "badge-info"
        val colors = listOf("#B22222", "#6441A5", "#C53121", "#00A7E6", "#E6D700")
    }

    fun getColor(): Int {
        if (tags[COLOR]!!.trim().isEmpty()) {
            return Color.parseColor(colors.random())
        }
        return Color.parseColor(tags[COLOR])
    }

    fun getDisplayName(): String {
        return tags[DISPLAY_NAME]!!
    }

    fun isSubscriber(): Boolean {
        return tags[SUBSCRIBER] != "0"
    }

    fun isTurbo(): Boolean {
        return tags[TURBO] != "0"
    }

    fun getBadgeInfo(): Int {
        val info = tags[BADGE_INFO] ?: return -1
        if (info.trim().isEmpty()) return -1
        return info.substring(11).toInt()
    }

    fun isMod(): Boolean {
        return tags[MOD] != "0"
    }
}