package com.example.semicolon

import android.provider.BaseColumns

object DBContract {

    /* Inner class that defines the table contents */
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "USER"
            const val COLUMN_USER_ID = "UserId"
            const val COLUMN_FIRST_NAME = "UserFirstName"
            const val COLUMN_LAST_NAME = "UserLastName"
            const val COLUMN_PHONE = "Phone"
            const val COLUMN_PASSWORD = "Password"
            const val COLUMN_CITY = "City"
            const val COLUMN_AGREEMENT_CHECK = "AgreementCheck"
            const val COLUMN_RATING = "Rating"
        }
    }
}