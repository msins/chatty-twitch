package edu.fer.unizg.msins.chatty.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MultipleChatsViewModel : ViewModel() {
    private val _currentChat = MutableLiveData<String>()
    val currentChat: LiveData<String>
        get() = _currentChat

    private val _chats = MutableLiveData<MutableList<String>>()
    val chats: LiveData<MutableList<String>>
        get() = _chats

    private val _eventChatAdded = MutableLiveData<String>()
    val eventChatAdded: LiveData<String>
        get() = _eventChatAdded

    init {
        _chats.value = ArrayList()
    }

    fun lastChatIndex(): Int {
        return _chats.value?.size?.minus(1)!!
    }

    fun addNewChat(chat: String) {
        _chats.value?.add(chat)
        _currentChat.value = chat
        _eventChatAdded.value = chat
    }
}