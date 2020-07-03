package com.example.semicolon.sqlite_database.daos

import androidx.room.*
import com.example.semicolon.sqlite_database.Follower

@Dao
interface FollowerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(follower: Follower)

    @Update
    fun update(follower: Follower)

    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowers(userId: Int): Int

    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowing(userId: Int): Int
}