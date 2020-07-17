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

/**
 * ViewModel for RequestsFragmentViewModel.
 *
 * @param userID The id of the current user
 */
class RequestsFragmentViewModel(private val userID: Int, private val followerDatabase: FollowerDao,
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
     * a [AndroidViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Contains the list of users who sent a request to the user with given userId.
     */
    val totalRequests = followerDatabase.getRequestListUsers(userID)

    /**
     * Variable that tells the Fragment to navigate to a specific SelectedUserFragment
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _userId = MutableLiveData<Int>()

    /**
     * If this is non-null, immediately navigate to SelectedFragmentFragment and call [onSelectedUserNavigated]
     */
    val userId
        get() = _userId

    /**
     * Contains the bitmap drawable of users picture
     */
    private var _bitmapDrawable = MutableLiveData<BitmapDrawable>()
    //getter
    val bitmapDrawable: LiveData<BitmapDrawable> get() = _bitmapDrawable

    init {
        checkUser(application)
    }


    /**
     * Retrieves users bitmap drawable
     */
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

    /**
     * Call this immediately after navigating to SelectedUserFragment.
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun onSelectedUserNavigated() {
        _userId.value = null
    }

    /**
     * Updates followers relationship record with a new condition
     */
    fun setNewCondition(senderId: Int, condition: Int) {
        uiScope.launch {
            updateRecord(senderId, userID, condition)
        }
    }

    fun deleteRecord(senderId: Int) {
        uiScope.launch {
            removeRecord(senderId, userID)
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