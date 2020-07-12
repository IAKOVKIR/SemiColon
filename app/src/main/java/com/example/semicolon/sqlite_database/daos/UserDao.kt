package com.example.semicolon.sqlite_database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.semicolon.models.DBContract
import com.example.semicolon.sqlite_database.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT COUNT(*) FROM USER LIMIT 1")
    fun getOne(): Int

    @Query("SELECT MAX(UserID) FROM USER")
    fun getMaxId(): Int

    @Query("SELECT * FROM USER WHERE UserID = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT * FROM USER WHERE Username = :username AND Password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("DELETE FROM USER")
    fun clear()

    @Query("SELECT * FROM USER WHERE Username = :Username AND Password = :Password LIMIT 1")
    fun findUserByUsernameAndPassword(Username: String, Password: String): User?

    @Query("SELECT * FROM USER INNER JOIN FOLLOWER ON USER.UserID = FOLLOWER.SenderID WHERE FOLLOWER.ReceiverID = :userId AND FOLLOWER.Condition = 0")
    fun getRequestListUsers(userId: Int): LiveData<List<User>>
}