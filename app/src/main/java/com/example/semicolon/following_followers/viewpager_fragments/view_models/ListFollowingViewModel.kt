package com.example.semicolon.following_followers.viewpager_fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class ListFollowingViewModel(private val myID: Int, private val followerDatabase: FollowerDao): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _totalFollowing = MutableLiveData<List<User>>()
    val totalFollowing: LiveData<List<User>> get() = _totalFollowing

    init {
        findFollowing(myID)
    }

    private fun findFollowing(myId: Int) {
        uiScope.launch {
            _totalFollowing.value = getFollowingList(myId)
        }
    }

    private suspend fun getFollowingList(myId: Int): List<User> {
        return withContext(Dispatchers.IO) {
            followerDatabase.readAllFollowing(myId)
        }
    }
}