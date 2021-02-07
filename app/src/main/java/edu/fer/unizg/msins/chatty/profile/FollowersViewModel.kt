package edu.fer.unizg.msins.chatty.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.fer.unizg.msins.chatty.networking.services.HelixService
import edu.fer.unizg.msins.chatty.networking.Retrofits
import edu.fer.unizg.msins.chatty.networking.ifSuccessful
import kotlinx.coroutines.*

class FollowersViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventFollowersFetched = MutableLiveData<List<String>>()
    val eventFollowersFetched: LiveData<List<String>>
        get() = _eventFollowersFetched

    fun fetchFollowers(oAuth: String, id: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val service = Retrofits.helix.create(HelixService::class.java)
                val response = service.getUserFollowers("Bearer $oAuth", id)
                response.ifSuccessful { dto ->
                    dto.data.let { followings ->
                        _eventFollowersFetched.postValue(followings.map { it.fromName })
                    }
                }
            }
        }
    }
}