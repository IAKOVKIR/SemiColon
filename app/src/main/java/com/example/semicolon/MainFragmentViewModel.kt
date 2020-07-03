package com.example.semicolon

import android.app.Application
import androidx.lifecycle.*
import com.example.semicolon.sqlite_database.*
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao
import kotlinx.coroutines.*

class MainFragmentViewModel(myID: Int, private val userDatabase: UserDao, private val followerDatabase: FollowerDao,
                            application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private var _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> get() = _followers

    private var _following = MutableLiveData<Int>()
    val following: LiveData<Int> get() = _following

    init {
        checkUser(myID)
    }

    private fun checkUser(userId: Int) {
        uiScope.launch {
            _user.value = getUser(userId)
            _followers.value = getTotalFollowers(userId)
            _following.value = getTotalFollowing(userId)
        }
    }

    private suspend fun getUser(userId: Int): User? {
        return withContext(Dispatchers.IO) {
            userDatabase.getUserById(userId)
        }
    }

    private suspend fun getTotalFollowers(userId: Int): Int {
        return withContext(Dispatchers.IO) {
            followerDatabase.getTotalFollowers(userId)
        }
    }

    private suspend fun getTotalFollowing(userId: Int): Int {
        return withContext(Dispatchers.IO) {
            followerDatabase.getTotalFollowing(userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}