package com.example.semicolon.following_followers.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class FollowingFollowersViewModel(userId: Int, private val followerDatabase: FollowerDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _totalRequests = MutableLiveData<Int>()
    val totalRequests: LiveData<Int> get() = _totalRequests

    init {
        getTotalRequests(userId)
    }

    private fun getTotalRequests(userId: Int) {
        uiScope.launch {
            _totalRequests.value = getRequests(userId)
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