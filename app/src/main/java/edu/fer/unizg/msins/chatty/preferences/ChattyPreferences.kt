package edu.fer.unizg.msins.chatty.preferences

import android.content.SharedPreferences
import edu.fer.unizg.msins.chatty.login.User

object ChattyPreferences {
    lateinit var prefs: SharedPreferences

    fun isFirstRun(): Boolean {
        return prefs.getBoolean(PrefsTag.KEY_FIRST_RUN, true)
    }

    fun setFirstRun(isFirstRun: Boolean) {
        prefs.edit().putBoolean(PrefsTag.KEY_FIRST_RUN, isFirstRun).apply()
    }

    fun setCurrentUser(user: User) {
        User.setCurrent(user)
        prefs.edit().putString(PrefsTag.KEY_USERNAME, user.name).apply()
        prefs.edit().putString(PrefsTag.KEY_USER_ID, user.id).apply()
        prefs.edit().putString(PrefsTag.KEY_OAUTH, user.oAuth).apply()
    }

    fun readCurrentUser() {
        val username = prefs.getString(PrefsTag.KEY_USERNAME, null)
        val userId = prefs.getString(PrefsTag.KEY_USER_ID, null)
        val oauth = prefs.getString(PrefsTag.KEY_OAUTH, null)

        if (username != null && oauth != null && userId != null) {
            User.setCurrent(User(oauth, userId, username))
        } else {
            User.setCurrent(User.ANONYMOUS)
        }
    }
}