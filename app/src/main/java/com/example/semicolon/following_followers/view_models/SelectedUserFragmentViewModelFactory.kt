package com.example.semicolon.following_followers.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao

/**
 * Provides the id for the current and selected Users, UserDao, FollowerDao and Application to the [SelectedUserFragmentViewModel].
 */
class SelectedUserFragmentViewModelFactory(private val myID: Int, private val userID: Int, private val userDataSource: UserDao,
                                           private val followerDataSource: FollowerDao, private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectedUserFragmentViewModel::class.java)) {
            return SelectedUserFragmentViewModel(myID, userID, userDataSource, followerDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}