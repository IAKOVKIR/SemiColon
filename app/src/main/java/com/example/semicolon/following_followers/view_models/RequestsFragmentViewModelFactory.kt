package com.example.semicolon.following_followers.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semicolon.sqlite_database.daos.FollowerDao

class RequestsFragmentViewModelFactory(private val myID: Int, private val followerDataSource: FollowerDao,
                                       private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequestsFragmentViewModel::class.java)) {
            return RequestsFragmentViewModel(myID, followerDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}