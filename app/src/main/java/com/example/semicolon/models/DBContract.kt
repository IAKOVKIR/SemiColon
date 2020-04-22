package com.example.semicolon.models

import android.provider.BaseColumns

object DBContract {
    // Inner class that defines the table contents
    class UserEntry : BaseColumns {
        companion object {
            //USER table
            const val USER_TABLE_NAME = "USER"
            const val USER_COLUMN_USER_ID = "UserID"
            const val USER_COLUMN_USERNAME = "Username"
            const val USER_COLUMN_PHONE = "Phone"
            const val USER_COLUMN_PASSWORD = "Password"
            const val USER_COLUMN_USER_FULL_NAME = "UserFullName"
            const val USER_COLUMN_BIO_DESCRIPTION = "BioDescription"
            const val USER_COLUMN_EMAIL = "Email"
            const val USER_COLUMN_RATING = "Rating"
            const val USER_COLUMN_LAST_MODIFIED = "LastModified"
            const val USER_COLUMN_DATE_CREATED = "DateCreated"

            //FOLLOWER table
            const val FOLLOWER_TABLE_NAME = "FOLLOWER"
            const val FOLLOWER_COLUMN_ID = "FollowerID"
            const val FOLLOWER_COLUMN_SENDER_ID = "SenderID"
            const val FOLLOWER_COLUMN_RECEIVER_ID = "ReceiverID"
            const val FOLLOWER_COLUMN_DATE = "Date"
            const val FOLLOWER_COLUMN_TIME = "Time"
            const val FOLLOWER_COLUMN_CONDITION = "Condition"

            //EVENT table
            const val EVENT_TABLE_NAME = "EVENT"
            const val EVENT_COLUMN_ID = "EventID"
            const val EVENT_COLUMN_NAME = "EventName"
            const val EVENT_COLUMN_MAX_ATTENDEES = "MaxAttendees"
            const val EVENT_COLUMN_LOCATION = "Location"
            const val EVENT_COLUMN_START_DATE = "StartDate"
            const val EVENT_COLUMN_END_DATE = "EndDate"
            const val EVENT_COLUMN_START_TIME = "StartTime"
            const val EVENT_COLUMN_END_TIME = "EndTime"
            const val EVENT_COLUMN_CURRENT_STATUS = "CurrentStatus" // active, expired, cancelled

            //ATTENDEES table
            const val ATTENDEE_TABLE_NAME = "ATTENDEES"
            const val ATTENDEE_COLUMN_ID = "AttendeesID"
            const val ATTENDEE_COLUMN_EVENT_ID = "EventID"
            const val ATTENDEE_COLUMN_USER_ID = "UserID"
            const val ATTENDEE_COLUMN_POSITION = "Position" //host, co-host, user
            const val ATTENDEE_COLUMN_CONDITION = "Condition" // sentToUser, sentToHost, accepted
            const val ATTENDEE_COLUMN_LAST_MODIFIED = "LastModified"
            const val ATTENDEE_COLUMN_DATE_ACCEPTED = "DateAccepted"
            const val ATTENDEE_COLUMN_DATE_CREATED = "DateCreated"
        }
    }
}