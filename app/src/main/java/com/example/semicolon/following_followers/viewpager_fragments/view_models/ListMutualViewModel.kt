package com.example.semicolon.following_followers.viewpager_fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.FollowerDao
import kotlinx.coroutines.*

/**
 * ViewModel for ListMutual.
 *
 * @param userId The id of the User that follows all the users that follow the user with given [selectedUserId]
 */
class ListMutualViewModel(private val userId: Int, private val selectedUserId: Int, private val followerDatabase: FollowerDao): ViewModel() {

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
     * Contains a list of users that follow the user with [selectedUserId] and are followed by the user with [userId].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the MainFragment.
     */
    private val _totalMutualFollowers = MutableLiveData<List<User>>()
    //getter
    val totalMutualFollowers: LiveData<List<User>> get() = _totalMutualFollowers

    init {
        findFollowing()
    }

    /**
     * Retrieves the list of users
     */
    private fun findFollowing() {
        /**
         * Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a Job.
         */
        uiScope.launch {
            _totalMutualFollowers.value = getMutualFollowersList()
        }
    }

    private suspend fun getMutualFollowersList(): List<User> {
        return withContext(Dispatchers.IO) {
            followerDatabase.readAllMutualFollowers(userId, selectedUserId)
        }
    }
}