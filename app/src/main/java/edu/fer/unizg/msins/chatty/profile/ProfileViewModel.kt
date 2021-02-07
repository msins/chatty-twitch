package edu.fer.unizg.msins.chatty.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.networking.services.HelixService
import edu.fer.unizg.msins.chatty.networking.Retrofits
import edu.fer.unizg.msins.chatty.networking.ifSuccessful
import kotlinx.coroutines.*

class ProfileViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventTwitchUserInfoFetched = MutableLiveData<String>()
    val eventTwitchUserInfoFetched: LiveData<String>
        get() = _eventTwitchUserInfoFetched

    fun getUserByOauth(oAuth: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val service = Retrofits.helix.create(HelixService::class.java)
                val response =
                    service.getUserInfo(
                        "Bearer $oAuth",
                        User.getCurrent().name
                    )
                //TODO bolje bi bilo koristiti npr onSuccess{} i onFail{}
                response.ifSuccessful {
                    val streamerTwitchInfo = response.body()!!.data[0]
                    _eventTwitchUserInfoFetched.postValue(streamerTwitchInfo.avatarUrl)
                }
            }
        }
    }
}