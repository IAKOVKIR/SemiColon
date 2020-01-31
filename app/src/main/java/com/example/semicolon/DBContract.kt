package com.example.semicolon

import android.provider.BaseColumns

object DBContract {

    // Inner class that defines the table contents
    class UserEntry : BaseColumns {
        companion object {
            //USER table
            const val USER_TABLE_NAME = "USER"
            const val USER_COLUMN_ID = "UserId"
            const val USER_COLUMN_USERNAME = "UserName"
            const val USER_COLUMN_FIRST_NAME = "UserFirstName"
            const val USER_COLUMN_LAST_NAME = "UserLastName"
            const val USER_COLUMN_PHONE = "Phone"
            const val USER_COLUMN_PASSWORD = "Password"
            const val USER_COLUMN_RATING = "Rating"
            const val USER_COLUMN_EMAIL = "Email"

            //ARTIST table

            //FRIEND table
            const val FRIEND_TABLE_NAME = "FRIEND"
            const val FRIEND_COLUMN_FRIEND_ID = "FriendshipID"
            const val FRIEND_COLUMN_SENDER_ID = "SenderID"
            const val FRIEND_COLUMN_RECEIVER_ID = "ReceiverID"
            const val FRIEND_COLUMN_DATE = "Date"
            const val FRIEND_COLUMN_TIME = "Time"
            const val FRIEND_COLUMN_CONDITION = "Condition"
        }
    }
}