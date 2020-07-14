package com.example.semicolon.sqlite_database

import androidx.room.*
import com.example.semicolon.models.DBContract

@Entity(tableName = DBContract.UserEntry.EVENT_MEDIA_TABLE_NAME,
    indices = [Index(value = [DBContract.UserEntry.FOLLOWER_COLUMN_ID], unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = [
        DBContract.UserEntry.USER_COLUMN_USER_ID], childColumns = [
        DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID], onDelete = ForeignKey.CASCADE)]
)
data class EventMedia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBContract.UserEntry.EVENT_MEDIA_ID)
    val EventMediaID: Int,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_MEDIA_EVENT_ID)
    val EventID: Int,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_MEDIA_FILE_NAME)
    val FileName: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_MEDIA_DATE_ADDED)
    val DateAdded: String
)