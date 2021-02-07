package edu.fer.unizg.msins.chatty.chat

import org.pircbotx.hooks.Event
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.ConnectEvent
import org.pircbotx.hooks.events.OutputEvent
import org.pircbotx.hooks.events.UnknownEvent
import org.pircbotx.hooks.types.GenericMessageEvent
import timber.log.Timber

class TwitchListenerAdapter(
    val onConnect: () -> Unit,
    val onNewMessage: (GenericMessageEvent?) -> Unit,
    val onSentMessage: (OutputEvent?) -> Unit
) : ListenerAdapter() {

    override fun onEvent(event: Event?) {
        if (event is UnknownEvent) {
            println(event)
        }
        super.onEvent(event)
    }

    override fun onOutput(event: OutputEvent?) {
        println(event)
        super.onOutput(event)
    }
    override fun onConnect(event: ConnectEvent?) {
        this.onConnect()
        super.onConnect(event)
    }

    override fun onGenericMessage(event: GenericMessageEvent?) {
        onNewMessage(event)
        super.onGenericMessage(event)
    }
}