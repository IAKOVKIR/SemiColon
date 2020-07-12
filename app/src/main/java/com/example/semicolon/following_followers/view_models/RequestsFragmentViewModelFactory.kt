package com.example.semicolon.following_followers.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semicolon.sqlite_database.daos.FollowerDao
import com.example.semicolon.sqlite_database.daos.UserDao

class RequestsFragmentViewModelFactory(private val myID: Int, private val userDataSource: UserDao,
                                       private val followerDataSource: FollowerDao, private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestsFragmentViewModel::class.java)) {
            return RequestsFragmentViewModel(myID, userDataSource, followerDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}