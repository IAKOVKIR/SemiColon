package com.example.semicolon

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.ArrayList

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_TABLE)
        db.execSQL(SQL_CREATE_FRIEND_TABLE)
        Log.d("CREATING TABLES :", "SUCCESS")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_FRIEND_TABLE)
        db.execSQL(SQL_DELETE_USER_TABLE)
        onCreate(db)
        Log.d("UPGRADING TABLE :", "SUCCESS")
    }

    @Throws(SQLiteConstraintException::class)
    fun insertRequest(friend: Friend): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.UserEntry.FRIEND_COLUMN_FRIEND_ID, friend.id)
        values.put(DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID, friend.SenderID)
        values.put(DBContract.UserEntry.FRIEND_COLUMN_RECEIVER_ID, friend.ReceiverID)
        values.put(DBContract.UserEntry.FRIEND_COLUMN_DATE, friend.date)
        values.put(DBContract.UserEntry.FRIEND_COLUMN_TIME, friend.time)
        values.put(DBContract.UserEntry.FRIEND_COLUMN_CONDITION, friend.condition)

        db.insert(DBContract.UserEntry.FRIEND_TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun setPassword(UserID: Int, newPassword: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("Password", newPassword)

        //updates the record in USER table
        db.update(DBContract.UserEntry.USER_TABLE_NAME, cv, "UserID = $UserID", null)

        //alternative way
        //db.execSQL("UPDATE USER SET Password = '$newPassword' WHERE UserID = '$UserID'")

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateRequest(SenderID: String, ReceiverID: String, condition: Int): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("Condition", condition)

        db.update(DBContract.UserEntry.FRIEND_TABLE_NAME, cv, "SenderID = $SenderID AND ReceiverID = $ReceiverID", null)

        //alternative way
        //db.execSQL("UPDATE USER SET Password = '$newPassword' WHERE UserID = '$UserID'")

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(UserID: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.UserEntry.USER_COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(UserID)
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.USER_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readUser(Phone: String, Password: String): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        val cursor: Cursor

        try {
            cursor = db.rawQuery("select * from ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_PHONE} = '$Phone' AND ${DBContract.UserEntry.USER_COLUMN_PASSWORD} = '$Password'", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        var id: Int
        var firstName: String
        var lastName: String
        var phone: String
        var password: String
        var city: String
        var agreementCheck: Byte
        var rating: Float
        var email: String

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                password = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                agreementCheck = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK)).toByte()
                rating = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                users.add(User(id, firstName, lastName, phone, password, city, agreementCheck, rating, email))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return users
    }

    fun readAllFollowers(UserID: String, num: Int, except: Int): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        val cursor: Cursor?

        val condition: Int = if (num == 1)
            1//confirmed
        else
            3//inProgress

        try {
            val line = "SELECT USER.UserID, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.City, USER.Email FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.SenderID WHERE FRIEND.ReceiverID = '$UserID' AND FRIEND.Condition = '$condition' AND FRIEND.SenderID != '$except'"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        var id: Int
        var firstName: String
        var lastName: String
        var phoneNum: String
        var city: String
        var email: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return users
    }

    fun searchAllFollowers(UserID: String, num: Int, except: Int, searchLine: String): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        val cursor: Cursor?

        val condition: Int = if (num == 1)
            1//confirmed
        else
            3//inProgress

        try {
            val line = "SELECT USER.UserID, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.City, USER.Email FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.SenderID WHERE FRIEND.ReceiverID = '$UserID' AND FRIEND.Condition = '$condition' AND FRIEND.SenderID != '$except' AND (USER.UserFirstName LIKE '$searchLine%' OR USER.UserLastName LIKE '$searchLine%')"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        var id: Int
        var firstName: String
        var lastName: String
        var phoneNum: String
        var city: String
        var email: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                cursor.moveToNext()
            }
        }

        cursor.close()
        return users
    }

    fun readAllFollowing(UserID: String): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT USER.UserID, USER.UserFirstName, USER.UserLastName FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.ReceiverID WHERE FRIEND.SenderID = '$UserID' AND FRIEND.Condition = 1", null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        var id: Int
        var firstName: String
        var lastName: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                users.add(User(id, firstName, lastName, "", "", "Melbourne", 1, 0F, ""))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return users
    }

    fun countFollowers(UserID: String): Int {
        var total = 0
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.SenderID WHERE FRIEND.ReceiverID = '$UserID' AND FRIEND.Condition = 1", null)
            if (cursor!!.moveToFirst()) total = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 0
        }

        cursor.close()
        return total
    }

    fun countFriendTable(): Int {
        var total = 0
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT * FROM FRIEND WHERE FriendshipID = (SELECT MAX(FriendshipID) FROM FRIEND)", null)
            if (cursor!!.moveToFirst()) total = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 0
        }

        cursor.close()
        return (total + 1)
    }

    fun countFollowing(UserID: String): Int {
        var total = 0
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.ReceiverID WHERE FRIEND.SenderID = '$UserID' AND FRIEND.Condition = 1", null)
            if (cursor!!.moveToFirst()) total = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 0
        }

        cursor.close()
        return total
    }

    fun deleteFollowing(SenderID: String, ReceiverID: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " = ? AND " + DBContract.UserEntry.FRIEND_COLUMN_RECEIVER_ID + " = ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(SenderID, ReceiverID)
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.FRIEND_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun checkFollower(SenderID: String, ReceiverID: String): Int {
        var condition = 0
        val db = writableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT Condition FROM FRIEND WHERE SenderID = '$SenderID' AND ReceiverID = '$ReceiverID'", null)
            if (cursor!!.moveToFirst()) condition = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 1
        }

        cursor.close()
        return condition
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        var DATABASE_VERSION = 1
        const val DATABASE_NAME = "SemiColon"

        private const val SQL_CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.USER_TABLE_NAME + " (" +
                    DBContract.UserEntry.USER_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_PHONE + " TEXT NOT NULL UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_CITY + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK + " NUMERIC NOT NULL DEFAULT 1, " +
                    DBContract.UserEntry.USER_COLUMN_RATING + " NUMERIC NOT NULL DEFAULT 5.0, " +
                    DBContract.UserEntry.USER_COLUMN_EMAIL + " TEXT)"

        private const val SQL_CREATE_FRIEND_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.FRIEND_TABLE_NAME + " (" +
                    DBContract.UserEntry.FRIEND_COLUMN_FRIEND_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " INTEGER NOT NULL CONSTRAINT " +
                    DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " REFERENCES " + DBContract.UserEntry.USER_TABLE_NAME + ", " +
                    DBContract.UserEntry.FRIEND_COLUMN_RECEIVER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_TIME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_CONDITION + " INTEGER NOT NULL DEFAULT 1)"

        private const val SQL_DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + DBContract.UserEntry.USER_TABLE_NAME
        private const val SQL_DELETE_FRIEND_TABLE = "DROP TABLE IF EXISTS " + DBContract.UserEntry.FRIEND_TABLE_NAME
    }

    /*@Throws(SQLiteConstraintException::class)
   fun insertUser(user: User): Boolean {
       // Gets the data repository in write mode
       val db = writableDatabase

       // Create a new map of values, where column names are the keys
       val values = ContentValues()
       values.put(DBContract.UserEntry.USER_COLUMN_ID, user.id)
       values.put(DBContract.UserEntry.USER_COLUMN_FIRST_NAME, user.firstName)
       values.put(DBContract.UserEntry.USER_COLUMN_LAST_NAME, user.lastName)
       values.put(DBContract.UserEntry.USER_COLUMN_PHONE, user.phone)
       values.put(DBContract.UserEntry.USER_COLUMN_PASSWORD, user.password)
       values.put(DBContract.UserEntry.USER_COLUMN_CITY, user.city)
       values.put(DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK, user.agreementCheck)
       values.put(DBContract.UserEntry.USER_COLUMN_RATING, user.rating)
       values.put(DBContract.UserEntry.USER_COLUMN_EMAIL, user.email)

       db.insert(DBContract.UserEntry.USER_TABLE_NAME, null, values)

       return true
   }*/

}