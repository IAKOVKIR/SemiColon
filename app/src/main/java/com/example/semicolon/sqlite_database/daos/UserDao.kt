package com.example.semicolon.sqlite_database.daos

import androidx.room.*
import com.example.semicolon.sqlite_database.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    //@Query("SELECT * FROM ${DBContract.UserEntry.USER_TABLE_NAME}")
    //fun get(): ArrayList<User>?

    @Query("SELECT COUNT(*) FROM USER LIMIT 1")
    fun getOne(): Int

    @Query("SELECT * FROM USER WHERE UserID = :userId")
    fun getUserById(userId: Int): User?

    @Query("SELECT * FROM USER WHERE Username = :username AND Password = :password")
    fun getUserByUsernameAndPassword(username: String, password: String): User?

    @Query("DELETE FROM USER")
    fun clear()

    @Query("SELECT * FROM USER WHERE Username = :Username AND Password = :Password LIMIT 1")
    fun findUserByUsernameAndPassword(Username: String, Password: String): User?
}