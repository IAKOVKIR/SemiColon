package com.example.semicolon.following_followers.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao
import kotlinx.coroutines.*

class RequestsFragmentViewModel(val myID: Int, userDatabase: UserDao, private val followerDatabase: FollowerDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val totalRequests = userDatabase.getRequestListUsers(myID)

    private val _userId = MutableLiveData<Int>()
    val userId
        get() = _userId

    fun onUserClicked(id: Int) {
        _userId.value = id
    }

    fun onSleepDataQualityNavigated() {
        _userId.value = null
    }

    fun setNewCondition(senderId: Int, condition: Int) {
        uiScope.launch {
            updateRecord(senderId, myID, condition)
        }
    }

    fun deleteRecord(senderId: Int) {
        uiScope.launch {
            removeRecord(senderId, myID)
        }
    }

    private suspend fun updateRecord(senderId: Int, receiverId: Int, condition: Int) {
        withContext(Dispatchers.IO) {
            followerDatabase.updateRecord(senderId, receiverId, condition)
        }
    }

    private suspend fun removeRecord(senderId: Int, receiverId: Int) {
        withContext(Dispatchers.IO) {
            followerDatabase.deleteRecord(senderId, receiverId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}