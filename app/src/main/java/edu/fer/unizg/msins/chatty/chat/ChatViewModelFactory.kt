package edu.fer.unizg.msins.chatty.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ChatViewModelFactory(
    private val twitchChannel: TwitchChannel
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel(twitchChannel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}