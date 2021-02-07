package edu.fer.unizg.msins.chatty.chat

import edu.fer.unizg.msins.chatty.login.User
import org.pircbotx.Configuration
import org.pircbotx.PircBotX
import org.pircbotx.cap.EnableCapHandler
import org.pircbotx.hooks.Listener

class TwitchChannel(val name: String, listener: Listener) {
    var bot: PircBotX

    init {
        var builder = Configuration.Builder()
            .setName(User.getCurrent().name)
            .addServer(IRC)
            .addAutoJoinChannel("#$name")
            .addListener(listener)
            .addCapHandler(EnableCapHandler("twitch.tv/tags"))

        if (User.getCurrent().oAuth.isNotEmpty()) {
            builder = builder.setServerPassword("oauth:${User.getCurrent().oAuth}")
        }

        val configuration = builder.buildConfiguration()
        bot = PircBotX(configuration)
    }

    companion object {
        const val IRC = "irc.twitch.tv"
    }

}