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

/**
 * ViewModel for MainFragment.
 *
 * @param userId The id of the current user
 */
class MainFragmentViewModel(userId: Int, private val userDatabase: UserDao, private val followerDatabase: FollowerDao,
                            application: Application): AndroidViewModel(application) {

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

    /**
     * Contains an object of User that Signed in
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the MainFragment.
     */
    private var _user = MutableLiveData<User?>()
    //getter
    val user: LiveData<User?> get() = _user

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
     * A Bitmap drawable of current User's picture
     */
    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    //getter
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    init {
        checkUser(userId, application)
    }

    /**
     * Retrieves Users data
     */
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
            var bitmap: Bitmap = BitmapFactory.decodeResource(application.resources, R.drawable.burns)
            val dif: Double = bitmap.height.toDouble() / bitmap.width
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
            BitmapDrawable(application.resources, bitmap)
        }
    }

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     *
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}