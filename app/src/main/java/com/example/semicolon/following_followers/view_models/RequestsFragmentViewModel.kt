package com.example.semicolon.following_followers.view_models

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.semicolon.R
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

class RequestsFragmentViewModel(private val myID: Int, private val followerDatabase: FollowerDao,
                                application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val totalRequests = followerDatabase.getRequestListUsers(myID)

    private val _userId = MutableLiveData<Int>()
    val userId
        get() = _userId

    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    init {
        checkUser(application)
    }

    private fun checkUser(application: Application) {
        uiScope.launch {
            _bitmapDrawable.value = getImage(application)
        }
    }

    private suspend fun getImage(application: Application): BitmapDrawable {
        return withContext(Dispatchers.Default) {
            var bitmap: Bitmap = BitmapFactory.decodeResource(application.resources, R.drawable.burns)
            val dif: Double = bitmap.height.toDouble() / bitmap.width
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
            BitmapDrawable(application.resources, bitmap)
        }
    }

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