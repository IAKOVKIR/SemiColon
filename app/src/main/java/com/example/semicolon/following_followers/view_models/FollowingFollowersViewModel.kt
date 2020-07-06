package com.example.semicolon.following_followers.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class FollowingFollowersViewModel(myID: Int, private val followerDatabase: FollowerDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _totalRequests = MutableLiveData<Int>()
    val totalRequests: LiveData<Int> get() = _totalRequests

    init {
        getTotalRequests(myID)
    }

    private fun getTotalRequests(myID: Int) {
        uiScope.launch {
            _totalRequests.value = getRequests(myID)
        }
    }

    private suspend fun getRequests(userId: Int): Int {
        return withContext(Dispatchers.IO) {
            followerDatabase.getTotalRequests(userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}