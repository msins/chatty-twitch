package edu.fer.unizg.msins.chatty.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.fer.unizg.msins.chatty.networking.services.HelixService
import edu.fer.unizg.msins.chatty.networking.Retrofits
import edu.fer.unizg.msins.chatty.networking.ifSuccessful
import kotlinx.coroutines.*

class FollowingViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventFollowsFetched = MutableLiveData<List<String>>()
    val eventFollowsFetched: LiveData<List<String>>
        get() = _eventFollowsFetched

    fun fetchUserFollows(oAuth: String, id: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val service = Retrofits.helix.create(HelixService::class.java)
                val response = service.getUserFollows("Bearer $oAuth", id)
                response.ifSuccessful { dto ->
                    dto.data.let { follows ->
                        _eventFollowsFetched.postValue(follows.map { it.toName })
                    }
                }
            }
        }
    }
}