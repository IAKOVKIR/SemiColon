package com.example.semicolon.sqlite_database.daos

import androidx.room.*
import com.example.semicolon.models.DBContract
import com.example.semicolon.sqlite_database.Follower

@Dao
interface FollowerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(follower: Follower)

    @Update
    fun update(follower: Follower)

    @Query("UPDATE FOLLOWER SET Condition = :condition WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun updateRecord(senderId: Int, receiverId: Int, condition: Int)

    @Query("DELETE FROM FOLLOWER WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun deleteRecord(senderId: Int, receiverId: Int)

    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowers(userId: Int): Int

    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowing(userId: Int): Int

    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '0'")
    fun getTotalRequests(userId: Int): Int

    @Query("SELECT Condition FROM FOLLOWER WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun checkFollower(senderId: Int, receiverId: Int): Int

    @Query("SELECT COUNT(*) FROM FOLLOWER WHERE ReceiverID = :receiverId AND SenderID = :senderId")
    fun isRecordExist(senderId: Int, receiverId: Int): Int

    @Query("SELECT COUNT(*) FROM FOLLOWER WHERE ReceiverID = :receiverId AND SenderID = :senderId AND Condition = :condition")
    fun isRecordExistWithCondition(senderId: Int, receiverId: Int, condition: Int): Int

    @Query("SELECT MAX(FollowerID) FROM FOLLOWER")
    fun getMaxId(): Int

    @Query("SELECT Q1.Username FROM (SELECT USER.Username FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :myId AND FOLLOWER.Condition = '1') AS Q1 INNER JOIN (SELECT USER.Username FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1') AS Q2 ON Q1.Username = Q2.Username LIMIT 5")
    fun getMutualFollowers(myId: Int, userId: Int): List<String>
}