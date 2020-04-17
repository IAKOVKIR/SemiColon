package com.example.semicolon.sqlite_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.semicolon.models.DBContract
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.models.EventContent

import java.util.ArrayList

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_TABLE)
        db.execSQL(SQL_CREATE_FOLLOWERS_TABLE)
        db.execSQL(SQL_CREATE_EVENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_FRIEND_TABLE)
        db.execSQL(SQL_DELETE_USER_TABLE)
        db.execSQL(SQL_DELETE_EVENT_TABLE)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: User): Boolean {

        // Gets the data repository in write mode
        val db: SQLiteDatabase = writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.UserEntry.USER_COLUMN_USERNAME, user.username)
        values.put(DBContract.UserEntry.USER_COLUMN_FIRST_NAME, user.firstName)
        values.put(DBContract.UserEntry.USER_COLUMN_LAST_NAME, user.lastName)
        values.put(DBContract.UserEntry.USER_COLUMN_PHONE, user.phone)
        values.put(DBContract.UserEntry.USER_COLUMN_PASSWORD, user.password)
        values.put(DBContract.UserEntry.USER_COLUMN_RATING, user.rating)
        values.put(DBContract.UserEntry.USER_COLUMN_EMAIL, user.email)

        db.insert(DBContract.UserEntry.USER_TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertEvent(event: EventContent.Event): Boolean {

        // Gets the data repository in write mode
        val db: SQLiteDatabase = writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.UserEntry.EVENT_COLUMN_ID, event.eventId)
        values.put(DBContract.UserEntry.EVENT_COLUMN_NAME, event.eventName)
        values.put(DBContract.UserEntry.EVENT_COLUMN_MAX_ATTENDEES, event.maxAttendees)
        values.put(DBContract.UserEntry.EVENT_COLUMN_LOCATION, event.location)
        values.put(DBContract.UserEntry.EVENT_COLUMN_START_DATE, event.startDate)
        values.put(DBContract.UserEntry.EVENT_COLUMN_END_DATE, event.endDate)
        values.put(DBContract.UserEntry.EVENT_COLUMN_START_TIME, event.startTime)
        values.put(DBContract.UserEntry.EVENT_COLUMN_END_TIME, event.endTime)

        db.insert(DBContract.UserEntry.EVENT_TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun insertRequest(friend: Friend): Boolean {

        val db: SQLiteDatabase = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_ID, friend.id)
        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID, friend.SenderID)
        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID, friend.ReceiverID)
        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_DATE, friend.date)
        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_TIME, friend.time)
        values.put(DBContract.UserEntry.FOLLOWER_COLUMN_CONDITION, friend.condition)

        db.insert(DBContract.UserEntry.FOLLOWER_TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun setPassword(UserID: Int, newPassword: String): Boolean {

        val cv = ContentValues()
        cv.put("Password", newPassword)

        //updates the record in USER table
        val db: SQLiteDatabase = writableDatabase
        db.update(DBContract.UserEntry.USER_TABLE_NAME, cv, "UserID = $UserID", null)

        //alternative way
        //db.execSQL("UPDATE USER SET Password = '$newPassword' WHERE UserID = '$UserID'")

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateRequest(SenderID: Int, ReceiverID: Int, condition: Int): Boolean {

        val cv = ContentValues()
        cv.put("Condition", condition)

        val db: SQLiteDatabase = writableDatabase
        db.update(DBContract.UserEntry.FOLLOWER_TABLE_NAME, cv, "SenderID = $SenderID AND ReceiverID = $ReceiverID", null)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteFollowingRequest(SenderID: Int, ReceiverID: Int): Boolean {

        val db: SQLiteDatabase = writableDatabase

        // Define 'where' part of query.
        val selection = "${DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID} = ? AND ${DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$SenderID", "$ReceiverID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.FOLLOWER_TABLE_NAME, selection, selectionArgs)

        //alternative way
        //db.execSQL("delete * from TABLE_CONTACTS where KEY_ID = "+contact+";");

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(UserID: Int): Boolean {

        val db: SQLiteDatabase = writableDatabase

        // Define 'where' part of query.
        val selection = "${DBContract.UserEntry.USER_COLUMN_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$UserID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.USER_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun findUserByUsernameAndPassword(Username: String, Password: String): User {
        val db: SQLiteDatabase = writableDatabase
        var users = User()
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_USERNAME} = '$Username' AND ${DBContract.UserEntry.USER_COLUMN_PASSWORD} = '$Password'", null)
        } catch (e: SQLiteException) {
            return users
        }

        if (cursor.moveToFirst())
            if (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val password = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                val rating = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                users = User(
                    id, username, firstName, lastName,
                    phone, password, rating, email
                )
            }

        cursor.close()
        return users
    }

    fun getUsersData(userID: Int, columnName: String): String {
        val db: SQLiteDatabase = writableDatabase
        var pieceOfData = ""
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT $columnName FROM ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_ID} = '$userID'", null)
        } catch (e: SQLiteException) {
            return pieceOfData
        }

        if (cursor.moveToFirst() && !cursor.isAfterLast)
            pieceOfData = cursor.getString(cursor.getColumnIndex(columnName))

        cursor.close()
        return pieceOfData
    }

    fun getEventsData(eventID: Int, columnName: String): String {
        val db: SQLiteDatabase = writableDatabase
        var pieceOfData = ""
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT $columnName FROM ${DBContract.UserEntry.EVENT_TABLE_NAME} WHERE ${DBContract.UserEntry.EVENT_COLUMN_ID} = '$eventID'", null)
        } catch (e: SQLiteException) {
            return pieceOfData
        }

        if (cursor.moveToFirst() && !cursor.isAfterLast)
            pieceOfData = cursor.getString(cursor.getColumnIndex(columnName))

        cursor.close()
        return pieceOfData
    }

    fun findUserByID(UserID: Int): User {
        var users = User()
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null

        try {
            val line = "SELECT * FROM ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_ID} = '$UserID'"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                if (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val firstName: String = cursor.getString(cursor.getColumnIndex(
                        DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phone: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val password: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                    val rating: Float = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                    users = User(
                        id, username, firstName, lastName,
                        phone, password, rating, email
                    )
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }

    }

    fun readAllFollowers(UserID: Int, num: Int/*, except: Int*/): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.UserName, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$UserID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '$num'"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val firstName: String = cursor.getString(cursor.getColumnIndex(
                        DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            firstName,
                            lastName,
                            phoneNum,
                            "-1",
                            -1.0F,
                            email
                        )
                    )
                    cursor.moveToNext()
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }
    }

    fun readAllFollowing(SenderID: Int, except: Int): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.UserName, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$SenderID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '-1'/* AND FRIEND.ReceiverID != '$except'*/"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val firstName: String = cursor.getString(cursor.getColumnIndex(
                        DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            firstName,
                            lastName,
                            phoneNum,
                            "-1",
                            -1.0F,
                            email
                        )
                    )
                    cursor.moveToNext()
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }
    }

    fun readAllEvents(): ArrayList<EventContent.Event> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val events = ArrayList<EventContent.Event>()

        try {
            val line = "SELECT * FROM EVENT"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val eventId: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_ID))
                    val eventName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_NAME))
                    val maxAttendees: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_MAX_ATTENDEES))
                    val location: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_LOCATION))
                    val startDate: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_START_DATE))
                    val endDate: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_END_DATE))
                    val startTime: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_START_TIME))
                    val endTime: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.EVENT_COLUMN_END_TIME))
                    events.add(
                        EventContent.Event(eventId, eventName, maxAttendees, location, startDate, endDate, startTime, endTime)
                    )
                    cursor.moveToNext()
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return events
        }
    }

    fun searchAllUsers(except: Int, searchLine: String): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT UserID, UserName, UserFirstName, UserLastName, Phone, Email FROM USER WHERE UserID != '$except' AND (UserFirstName LIKE '$searchLine%' OR UserLastName LIKE '$searchLine%')"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val username = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            firstName,
                            lastName,
                            phoneNum,
                            "",
                            0F,
                            email
                        )
                    )
                    cursor.moveToNext()
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }

    }

    fun readFirstTenUsers(searchLine: String): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.UserName, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.Email FROM USER LIMIT 10"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            db.close()
            cursor!!.close()
            return ArrayList()
        }

        if (cursor.moveToFirst())
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(
                    User(
                        id,
                        username,
                        firstName,
                        lastName,
                        phoneNum,
                        "",
                        0F,
                        email
                    )
                )
                cursor.moveToNext()
            }

        db.close()
        cursor!!.close()
        return users
    }

    fun countFollowers(UserID: Int): Int {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        var total = 0

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM ${DBContract.UserEntry.USER_TABLE_NAME} INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} " +
                    "ON ${DBContract.UserEntry.USER_TABLE_NAME}.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$UserID' " +
                    "AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '-1'", null)
            if (cursor.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            return total
        }
    }

    fun countFollowing(UserID: Int): Int {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        var total = 0

        try {
            val line = "SELECT COUNT(*) FROM ${DBContract.UserEntry.USER_TABLE_NAME} INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} " +
                    "ON ${DBContract.UserEntry.USER_TABLE_NAME}.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID WHERE " +
                    "${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$UserID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '-1'"
            cursor = db.rawQuery(line, null)
            if (cursor.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return total
        }
    }

    fun countFollowingRequests(UserID: Int): Int {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        var total = 0

        try {
            val line = "SELECT COUNT(*) FROM ${DBContract.UserEntry.USER_TABLE_NAME} INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON ${DBContract.UserEntry.USER_TABLE_NAME}.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$UserID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '0'"
            cursor = db.rawQuery(line, null)
            if (cursor.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return total
        }
    }

    fun countFriendTable(): Int {
        val db: SQLiteDatabase = writableDatabase
        val cursor: Cursor?
        var total = 0

        try {
            val line = "SELECT * FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} WHERE FriendshipID = (SELECT MAX(FriendshipID) FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME})"
            cursor = db.rawQuery(line, null)
            if (cursor.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 0
        }

        cursor.close()
        return total + 1
    }

    fun deleteFollowing(SenderID: Int, ReceiverID: Int): Boolean {

        val db: SQLiteDatabase = writableDatabase
        // Define 'where' part of query.
        val selection: String = "${DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID} = ? AND ${DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$SenderID", "$ReceiverID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.FOLLOWER_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun checkFollower(SenderID: Int, ReceiverID: Int): Int {
        val db: SQLiteDatabase = writableDatabase
        val cursor: Cursor?
        var condition = 0

        try {
            val line = "SELECT Condition FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} WHERE SenderID = '$SenderID' AND ReceiverID = '$ReceiverID'"
            cursor = db.rawQuery(line, null)
            if (cursor.moveToFirst())
                condition = cursor.getInt(0)
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

        private const val SQL_CREATE_USER_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.USER_TABLE_NAME + " (" +
                    DBContract.UserEntry.USER_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_PHONE + " TEXT UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_RATING + " NUMERIC NOT NULL DEFAULT 5.0, " +
                    DBContract.UserEntry.USER_COLUMN_EMAIL + " TEXT NOT NULL UNIQUE)"

        private const val SQL_CREATE_FOLLOWERS_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.FOLLOWER_TABLE_NAME + " (" +
                    DBContract.UserEntry.FOLLOWER_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID + " INTEGER NOT NULL CONSTRAINT " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID + " REFERENCES " + DBContract.UserEntry.USER_TABLE_NAME + ", " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_TIME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_CONDITION + " INTEGER NOT NULL DEFAULT 0)"

        private const val SQL_CREATE_EVENT_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.EVENT_TABLE_NAME + " (" +
                    DBContract.UserEntry.EVENT_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.EVENT_COLUMN_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_MAX_ATTENDEES + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_LOCATION + " TEXT NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_START_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_END_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_START_TIME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.EVENT_COLUMN_END_TIME + " TEXT NOT NULL)"

        private const val SQL_DELETE_USER_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.USER_TABLE_NAME
        private const val SQL_DELETE_FRIEND_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.FOLLOWER_TABLE_NAME
        private const val SQL_DELETE_EVENT_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.EVENT_TABLE_NAME
    }

}