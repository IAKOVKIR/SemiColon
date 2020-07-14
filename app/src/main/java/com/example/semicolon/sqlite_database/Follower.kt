package com.example.semicolon.sqlite_database

import androidx.room.*
import com.example.semicolon.models.DBContract

@Entity(tableName = DBContract.UserEntry.FOLLOWER_TABLE_NAME,
    indices = [Index(value = [DBContract.UserEntry.FOLLOWER_COLUMN_ID], unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = [
    DBContract.UserEntry.USER_COLUMN_USER_ID], childColumns = [
        DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = [
            DBContract.UserEntry.USER_COLUMN_USER_ID], childColumns = [
            DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID],
            onDelete = ForeignKey.CASCADE)]
)
data class Follower(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_ID)
    val FollowerID : Int,

    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID)
    val SenderID : Int,

    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID)
    val ReceiverID : Int,

    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_CONDITION)
    val Condition : Int,

    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_DATE_ACCEPTED)
    val DateAccepted : String,

    @ColumnInfo(name = DBContract.UserEntry.FOLLOWER_COLUMN_DATE_CREATED)
    val DateCreated : String) {
    constructor(senderId: Int, receiverId: Int, condition: Int, dateAccepted: String, dateCreated: String): this(-1, senderId, receiverId, condition, dateAccepted, dateCreated)
}