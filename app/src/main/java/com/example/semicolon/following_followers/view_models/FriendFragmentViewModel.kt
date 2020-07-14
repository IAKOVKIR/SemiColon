package com.example.semicolon.following_followers.view_models

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.semicolon.R
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.*

class FriendFragmentViewModel(myID: Int, userID: Int, private val userDatabase: UserDao,
                              private val followerDatabase: FollowerDao, application: Application
): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /*
        Variable bool contains one of three conditions of request:
        "1" - you follow the user
        "0" - "your request is in progress"
        "-1" - "you do not follow the user"
        */
    //Array str contains 3 of these conditions
    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")//-1, 0, 1
    private var bool = -1
    val time = Time()

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    private var _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> get() = _phoneNumber

    private var _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> get() = _followers

    private var _following = MutableLiveData<Int>()
    val following: LiveData<Int> get() = _following

    private var _followedByLine = MutableLiveData<Spanned>()
    val followedByLine: LiveData<Spanned> get() = _followedByLine

    private var _followUnFollowText = MutableLiveData<String>()
    val followUnFollowText: LiveData<String> get() = _followUnFollowText

    init {
        checkUser(myID, userID, application)
    }

    private fun checkUser(myId: Int, userId: Int, application: Application) {
        uiScope.launch {
            _user.value = getUser(userId)
            _bitmapDrawable.value = getImage(application)
            _phoneNumber.value = getPhone(user.value?.phone)
            _followers.value = getTotalFollowers(userId)
            _following.value = getTotalFollowing(userId)
            _followedByLine.value = getMutualFollowersLine(myId, userId)
            bool = checkFollower(myId, userId)
            buttonSetValue(bool)
        }
    }

    fun followOrUnFollow(myId: Int, userId: Int) {
        uiScope.launch {
            if (bool == -1) {
                if (sendRequest(myId, userId))
                    buttonSetValue(0)
            } else if (deleteRequest(myId, userId)) {
                buttonSetValue(-1)
            }
        }
    }

    private suspend fun getUser(userId: Int): User? {
        return withContext(Dispatchers.IO) {
            userDatabase.getUserById(userId)
        }
    }

    private suspend fun getPhone(str: String?): String {
        return withContext(Dispatchers.IO) {
            //variable phoneImp contains a string of phone number ("#(###)### ###")
            var line = ""
            if (str != null) {
                if (str.length > 9)
                    line = "${str[0]}(${str.substring(1, 4)})${str.substring(4, 7)} ${str.substring(7, 10)}"
            }
            line
        }
    }

    private suspend fun getImage(application: Application): BitmapDrawable {
        return withContext(Dispatchers.Default) {
            var bitmap: Bitmap = BitmapFactory.decodeResource(application.resources, R.drawable.smithers)
            val dif: Double = bitmap.height.toDouble() / bitmap.width
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
            BitmapDrawable(application.resources, bitmap)
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

    private suspend fun getTotalMutualFollowers(myId: Int, userId: Int): List<String> {
        return withContext(Dispatchers.IO) {
            followerDatabase.getMutualFollowers(myId, userId)
        }
    }

    private suspend fun getMutualFollowersLine(myId: Int, userId: Int): Spanned {
        val len = getTotalMutualFollowers(myId, userId)
        val line = when {
            len.size == 3 -> "Followed by <b>${len[0]}</b>, <b>${len[1]}</b> and <b>1 other</b>"
            len.size == 2 -> "Followed by <b>${len[0]}</b> and <b>${len[1]}</b>"
            len.size == 1 -> "Followed by <b>${len[0]}</b>"
            len.size > 3 -> "Followed by <b>${len[0]}</b>, <b>${len[1]}</b> and <b>${len.size - 2} others</b>"
            else -> ""
        }
        return HtmlCompat.fromHtml(line, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private suspend fun checkFollower(myId: Int, userId: Int): Int {
        return withContext(Dispatchers.IO) {
            if (followerDatabase.isRecordExist(myId, userId) == 1) {
                bool = followerDatabase.checkFollower(myId, userId)
            }
            bool
        }
    }

    private suspend fun sendRequest(myId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val friend = Follower(
                followerDatabase.getMaxId() + 1, myId, userId, 0,
                time.toString(), time.toString()
            )
            followerDatabase.insert(friend)

            followerDatabase.isRecordExistWithCondition(myId, userId, 0) == 1
        }
    }

    private suspend fun deleteRequest(myId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            followerDatabase.deleteRecord(myId, userId)

            followerDatabase.isRecordExistWithCondition(myId, userId, 0) == 0
        }
    }

    private fun buttonSetValue(newBool: Int) {
        bool = newBool
        _followUnFollowText.value = str[bool + 1]
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}