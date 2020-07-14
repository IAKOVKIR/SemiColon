package com.example.semicolon.sqlite_database

import androidx.room.*
import com.example.semicolon.models.DBContract

@Entity(tableName = DBContract.UserEntry.ATTENDEE_TABLE_NAME,
    indices = [Index(value = [DBContract.UserEntry.ATTENDEE_COLUMN_ID], unique = true)],
    foreignKeys = [ForeignKey(entity = Event::class, parentColumns = [
        DBContract.UserEntry.EVENT_COLUMN_ID], childColumns = [
        DBContract.UserEntry.ATTENDEE_COLUMN_EVENT_ID], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = [
            DBContract.UserEntry.USER_COLUMN_USER_ID], childColumns = [
            DBContract.UserEntry.ATTENDEE_COLUMN_USER_ID],
            onDelete = ForeignKey.CASCADE)]
)
data class Attendee(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_ID)
    val AttendeeID: Int,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_EVENT_ID)
    val EventID: Int,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_USER_ID)
    val UserID: Int,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_POSITION)
    val Position: String,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_CONDITION)
    val Condition: String,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_LAST_MODIFIED)
    val LastModified: String,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_DATE_ACCEPTED)
    val DateAccepted: String,

    @ColumnInfo(name = DBContract.UserEntry.ATTENDEE_COLUMN_DATE_CREATED)
    val DateCreated: String
)