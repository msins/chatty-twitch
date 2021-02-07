package edu.fer.unizg.msins.chatty

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.preferences.ChattyPreferences
import edu.fer.unizg.msins.chatty.preferences.PrefsTag

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ChattyPreferences.prefs = getSharedPreferences(PrefsTag.KEY_PREFS, Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        ChattyPreferences.readCurrentUser()
    }

    override fun onStop() {
        super.onStop()
        ChattyPreferences.setCurrentUser(User.getCurrent())
    }
}
