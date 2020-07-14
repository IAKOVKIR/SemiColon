package com.example.semicolon.sqlite_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.semicolon.models.DBContract

@Entity(tableName = DBContract.UserEntry.EVENT_TABLE_NAME,
    indices = [Index(value = [DBContract.UserEntry.EVENT_COLUMN_ID], unique = true)]
)
data class Event(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_ID)
    val EventID: Int,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_NAME)
    val EventName: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_MAX_ATTENDEES)
    val MaxAttendees: Int,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_LOCATION)
    val Location: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_DESCRIPTION)
    val Description: String?,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_START_DATE)
    val StartDate: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_END_DATE)
    val EndDate: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_START_TIME)
    val StartTime: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_END_TIME)
    val EndTime: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_LAST_MODIFIED)
    val LastModified: String,

    @ColumnInfo(name = DBContract.UserEntry.EVENT_COLUMN_DATE_CREATED)
    val DateCreated: String
)