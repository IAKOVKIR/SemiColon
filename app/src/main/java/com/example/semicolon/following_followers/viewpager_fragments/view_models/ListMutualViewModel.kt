package com.example.semicolon.following_followers.viewpager_fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class ListMutualViewModel(private val myID: Int, private val userID: Int, private val followerDatabase: FollowerDao): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _totalMutualFollowers = MutableLiveData<List<User>>()
    val totalMutualFollowers: LiveData<List<User>> get() = _totalMutualFollowers

    init {
        findFollowing()
    }

    private fun findFollowing() {
        uiScope.launch {
            _totalMutualFollowers.value = getMutualFollowersList()
        }
    }

    private suspend fun getMutualFollowersList(): List<User> {
        return withContext(Dispatchers.IO) {
            followerDatabase.readAllMutualFollowers(myID, userID)
        }
    }
}