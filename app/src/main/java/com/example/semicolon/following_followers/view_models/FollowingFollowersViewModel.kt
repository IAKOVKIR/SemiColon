package com.example.semicolon.following_followers.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

/**
 * ViewModel for FollowingFollowersViewModel.
 *
 * @param userId The id of the current user
 */
class FollowingFollowersViewModel(userId: Int, private val followerDatabase: FollowerDao) : ViewModel() {

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
     * Contains the number of users who sent a request to the user with given userId.
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the FollowingFollowersFragment.
     */
    private var _totalRequests = MutableLiveData<Int>()
    //getter
    val totalRequests: LiveData<Int> get() = _totalRequests

    init {
        getTotalRequests(userId)
    }

    /**
     * Retrieves the total number of requests
     */
    private fun getTotalRequests(userId: Int) {
        uiScope.launch {
            _totalRequests.value = getRequests(userId)
        }
    }

    private suspend fun getRequests(userId: Int): Int {
        return withContext(Dispatchers.IO) {
            followerDatabase.getTotalRequests(userId)
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