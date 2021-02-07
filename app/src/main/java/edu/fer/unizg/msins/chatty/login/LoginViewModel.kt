package edu.fer.unizg.msins.chatty.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _eventAnonymousLogin = MutableLiveData<Boolean>()
    val eventAnonymousLogin: LiveData<Boolean>
        get() = _eventAnonymousLogin

    private val _eventTwitchLogin = MutableLiveData<Boolean>()
    val eventTwitchLogin: LiveData<Boolean>
        get() = _eventTwitchLogin

    fun onAnonymousLogin() {
        _eventAnonymousLogin.value = true
    }

    fun onTwitchLogin() {
        _eventTwitchLogin.value = true
    }
}