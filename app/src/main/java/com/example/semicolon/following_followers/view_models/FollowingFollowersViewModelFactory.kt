package com.example.semicolon.following_followers.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semicolon.sqlite_database.daos.FollowerDao

class FollowingFollowersViewModelFactory(private val myID: Int, private val followerDataSource: FollowerDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowingFollowersViewModel::class.java)) {
            return FollowingFollowersViewModel(
                myID,
                followerDataSource
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}