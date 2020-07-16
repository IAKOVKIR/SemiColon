package com.example.semicolon.sqlite_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.semicolon.models.DBContract

/**
 * A User item representing a piece of pos.
 */
@Entity(tableName = DBContract.UserEntry.USER_TABLE_NAME,
    indices = [Index(value = [DBContract.UserEntry.USER_COLUMN_USER_ID,
        DBContract.UserEntry.USER_COLUMN_USERNAME,
        DBContract.UserEntry.USER_COLUMN_PHONE],
        unique = true)]
)
data class User(
    //Primary key that assigns automatic ids
    @PrimaryKey(autoGenerate = true)
    //Sets a different name
    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_USER_ID)
    val userId: Int,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_USERNAME)
    val username: String,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_PHONE)
    val phone : String,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_PASSWORD)
    val password : String,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME)
    val fullName: String?,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_BIO_DESCRIPTION)
    val bioDescription : String?,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_EMAIL)
    val email: String?,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_RATING, defaultValue = "5.0")
    val rating : Float,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_LAST_MODIFIED)
    val lastModified : String,

    @ColumnInfo(name = DBContract.UserEntry.USER_COLUMN_DATE_CREATED)
    val dateCreated : String) {

    //constructors
    constructor(): this(-1, "-1", "-1", "-1", "-1", "-1", "-1", -1.0F,"-1","-1")
    constructor(username: String, password : String, email: String): this (-1, username, "-1", password, "-1", "-1", email, 5.0F, "-1", "-1")
    constructor(username: String, phone : String, password : String, fullName: String, bioDescription : String, email: String, rating : Float, lastModified : String, dateCreated : String): this(-1, username, phone, password, fullName, bioDescription, email, rating, lastModified, dateCreated)
}