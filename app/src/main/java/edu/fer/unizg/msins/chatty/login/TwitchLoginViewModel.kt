package edu.fer.unizg.msins.chatty.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.fer.unizg.msins.chatty.networking.services.OAuthService
import edu.fer.unizg.msins.chatty.networking.Retrofits
import edu.fer.unizg.msins.chatty.networking.ifSuccessful
import kotlinx.coroutines.*

class TwitchLoginViewModel : ViewModel(){
    private val _eventUserVerificationStarted = MutableLiveData<Boolean>()
    val eventUserVerificationStarted: LiveData<Boolean>
        get() = _eventUserVerificationStarted

    private val _eventUserVerified = MutableLiveData<User>()
    val eventUserVerified: LiveData<User>
        get() = _eventUserVerified

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun verifyUser(oAuth: String) {
        _eventUserVerificationStarted.value = true
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val service = Retrofits.oauth.create(OAuthService::class.java)
                val response = service.getValidatedUser("OAuth $oAuth", OAuthService.BASE_URL)
                response.ifSuccessful {
                    _eventUserVerified.postValue(User(oAuth, it.userId.toString(), it.login))
                }
            }
        }
    }
}