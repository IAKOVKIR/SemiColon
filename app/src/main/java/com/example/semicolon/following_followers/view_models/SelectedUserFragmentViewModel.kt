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
import androidx.lifecycle.ViewModel
import com.example.semicolon.R
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.*

/**
 * ViewModel for MainFragment.
 *
 * @param userId The id of the current user
 * @param selectedUserId The id of the selected user
 */
class SelectedUserFragmentViewModel(userId: Int, selectedUserId: Int, private val userDatabase: UserDao,
                                    private val followerDatabase: FollowerDao, application: Application
): AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /** Variable bool contains one of three conditions of request:
     * '1' - you follow the user
     * '0' - your request is in progress
     * '-1' - you do not follow the user
     */

    //Array str contains 3 of these conditions
    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")//-1, 0, 1
    //The condition of relationship between the selected user and the current one
    private var bool = -1
    private val time = Time()

    /**
     * Contains an object of User that was selected
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the SelectedUserFragment.
     */
    private var _user = MutableLiveData<User?>()
    //getter
    val user: LiveData<User?> get() = _user

    /**
     * A Bitmap drawable of selected User's picture
     */
    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    //getter
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    /**
     * Contains User's formatted phone number
     */
    private var _phoneNumber = MutableLiveData<String>()
    //getter
    val phoneNumber: LiveData<String> get() = _phoneNumber

    /**
     * Contains the number of User's followers
     */
    private var _followers = MutableLiveData<Int>()
    //getter
    val followers: LiveData<Int> get() = _followers

    /**
     * Contains the number of users that the current User follows
     */
    private var _following = MutableLiveData<Int>()
    //getter
    val following: LiveData<Int> get() = _following

    /**
     * Formatted text that displays first 2 usernames of users that follow selected user and are followed by a current user
     */
    private var _followedByLine = MutableLiveData<Spanned>()
    //getter
    val followedByLine: LiveData<Spanned> get() = _followedByLine

    /**
     * displays the condition of relationship between selected and current users
     */
    private var _followUnFollowText = MutableLiveData<String>()
    //getter
    val followUnFollowText: LiveData<String> get() = _followUnFollowText

    init {
        checkUser(userId, selectedUserId, application)
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