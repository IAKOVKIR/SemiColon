package com.example.semicolon.sqlite_database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.sqlite_database.User

/**
 * Defines methods for using the Follower class with Room.
 */
@Dao
interface FollowerDao {

    /**
     * inserts a new follower record into the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(follower: Follower)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param follower new value to write
     */
    @Update
    fun update(follower: Follower)

    /**
     * Replaces the old condition value with the new one in a record with given senderId and receiverId.
     *
     * @param condition new value to write
     */
    @Query("UPDATE FOLLOWER SET Condition = :condition WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun updateRecord(senderId: Int, receiverId: Int, condition: Int)

    /**
     * Deletes the records with given senderId and receiverId.
     */
    @Query("DELETE FROM FOLLOWER WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun deleteRecord(senderId: Int, receiverId: Int)

    /**
     * Selects and returns the number of users that follow the user with given userId.
     */
    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowers(userId: Int): Int

    /**
     * Selects and returns the number of users that the user with given userId follows.
     */
    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :userId AND FOLLOWER.Condition = '1'")
    fun getTotalFollowing(userId: Int): Int

    /**
     * Selects and returns the number of users that sent a request to a user with given userId.
     */
    @Query("SELECT COUNT(*) FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '0'")
    fun getTotalRequests(userId: Int): Int

    /**
     * Selects and returns the condition of a relationship between users with given senderId and receiverId.
     */
    @Query("SELECT Condition FROM FOLLOWER WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun checkFollower(senderId: Int, receiverId: Int): Int

    /**
     * Returns the number of records with given senderId and receiverId.
     */
    @Query("SELECT COUNT(*) FROM FOLLOWER WHERE SenderID = :senderId AND ReceiverID = :receiverId")
    fun isRecordExist(senderId: Int, receiverId: Int): Int

    /**
     * Returns the number of records with given senderId, receiverId and condition.
     */
    @Query("SELECT COUNT(*) FROM FOLLOWER WHERE ReceiverID = :receiverId AND SenderID = :senderId AND Condition = :condition")
    fun isRecordExistWithCondition(senderId: Int, receiverId: Int, condition: Int): Int

    /**
     * Selects and returns the max FollowerID.
     */
    @Query("SELECT MAX(FollowerID) FROM FOLLOWER")
    fun getMaxId(): Int

    /**
     * Selects and returns the list with usernames of users that follow the user with given userId
     * and that are followed by user with given myId.
     */
    @Query("SELECT Q1.Username FROM (SELECT USER.Username FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :myId AND FOLLOWER.Condition = '1') AS Q1 INNER JOIN (SELECT USER.Username FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1') AS Q2 ON Q1.Username = Q2.Username LIMIT 5")
    fun getMutualFollowers(myId: Int, userId: Int): List<String>

    /**
     * Selects and returns the list of users that follow the user with given userId and num(as a Condition).
     */
    @Query("SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :UserID AND FOLLOWER.Condition = :num")
    fun readAllFollowers(UserID: Int, num: Int): List<User>

    /**
     * Selects and returns the list of users that the user with given SenderID follows.
     */
    @Query("SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :SenderID AND FOLLOWER.Condition = '1'")
    fun readAllFollowing(SenderID: Int): List<User>

    @Query("SELECT * FROM (SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.ReceiverID WHERE FOLLOWER.SenderID = :myId AND FOLLOWER.Condition = '1') AS Q1 INNER JOIN (SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = '1') AS Q2 ON Q1.UserID = Q2.UserID")
    fun readAllMutualFollowers(myId: Int, userId: Int): List<User>

    /**
     * Selects and returns the list of users that sent a request to a user with given userId.
     */
    @Query("SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = 0")
    fun getRequestListUsers(userId: Int): LiveData<List<User>>
}