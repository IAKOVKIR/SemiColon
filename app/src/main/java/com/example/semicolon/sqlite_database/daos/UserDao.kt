package com.example.semicolon.sqlite_database.daos

import androidx.room.*
import com.example.semicolon.sqlite_database.User

/**
 * Defines methods for using the User class with Room.
 */
@Dao
interface UserDao {

    /**
     * inserts a new user record into the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param user new value to write
     */
    @Update
    fun update(user: User)

    /**
     * Selects and returns number one if there is at least one user.
     */
    @Query("SELECT COUNT(*) FROM USER LIMIT 1")
    fun getOne(): Int

    /**
     * Selects and returns the max UserID.
     */
    @Query("SELECT MAX(UserID) FROM USER")
    fun getMaxId(): Int

    /**
     * Selects and returns the user with given userId.
     */
    @Query("SELECT * FROM USER WHERE UserID = :userId")
    fun getUserById(userId: Int): User?

    /**
     * Selects and returns the user with given Username and Password.
     */
    @Query("SELECT * FROM USER WHERE Username = :username AND Password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?

    /**
     * removes all the records from the table.
     */
    @Query("DELETE FROM USER")
    fun clear()
}