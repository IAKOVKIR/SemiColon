package com.example.semicolon.following_followers.viewpager_fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class ListFollowersViewModel(myID: Int, private val followerDatabase: FollowerDao): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _totalFollowers = MutableLiveData<List<User>>()
    val totalFollowers: LiveData<List<User>> get() = _totalFollowers

    init {
        findFollowers(myID)
    }

    private fun findFollowers(myId: Int) {
        uiScope.launch {
            _totalFollowers.value = updateRecord(myId)
        }
    }

    private suspend fun updateRecord(myId: Int): List<User> {
        return withContext(Dispatchers.IO) {
            followerDatabase.readAllFollowers(myId, 1)
        }
    }
}