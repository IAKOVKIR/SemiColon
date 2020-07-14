package com.example.semicolon.following_followers.viewpager_fragments.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semicolon.sqlite_database.daos.FollowerDao

class ListMutualViewModelFactory(private val myID: Int, private val userID: Int, private val followerDataSource: FollowerDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListMutualViewModel::class.java)) {
            return ListMutualViewModel(myID, userID, followerDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}