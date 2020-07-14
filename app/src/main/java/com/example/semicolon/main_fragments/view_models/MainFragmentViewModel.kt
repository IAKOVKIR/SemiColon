package com.example.semicolon.main_fragments.view_models

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.*
import com.example.semicolon.R
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

    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    init {
        checkUser(myID, application)
    }

    private fun checkUser(userId: Int, application: Application) {
        uiScope.launch {
            _user.value = getUser(userId)
            _followers.value = getTotalFollowers(userId)
            _following.value = getTotalFollowing(userId)
            _bitmapDrawable.value = getImage(application)
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

    private suspend fun getImage(application: Application): BitmapDrawable {
        return withContext(Dispatchers.Default) {
            var bitmap: Bitmap = BitmapFactory.decodeResource(application.resources,
                R.drawable.burns
            )
            val dif: Double = bitmap.height.toDouble() / bitmap.width
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
            BitmapDrawable(application.resources, bitmap)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}