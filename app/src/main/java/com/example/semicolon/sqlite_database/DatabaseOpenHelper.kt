package com.example.semicolon.sqlite_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.semicolon.models.*

import java.util.ArrayList

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_TABLE)
        db.execSQL(SQL_CREATE_FOLLOWER_TABLE)
        db.execSQL(SQL_CREATE_EVENT_TABLE)
        db.execSQL(SQL_CREATE_ATTENDEE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_USER_TABLE)
        db.execSQL(SQL_DELETE_FOLLOWER_TABLE)
        db.execSQL(SQL_DELETE_EVENT_TABLE)
        db.execSQL(SQL_DELETE_ATTENDEE_TABLE)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: User): Boolean {

        // Gets the data repository in write mode
        val db: SQLiteDatabase = writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.UserEntry.USER_COLUMN_USERNAME, user.username)
        values.put(DBContract.UserEntry.USER_COLUMN_PHONE, user.phone)
        values.put(DBContract.UserEntry.USER_COLUMN_PASSWORD, user.password)
        values.put(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME, user.fullName)
        values.put(DBContract.UserEntry.USER_COLUMN_BIO_DESCRIPTION, user.bioDescription)
        values.put(DBContract.UserEntry.USER_COLUMN_EMAIL, user.email)
        values.put(DBContract.UserEntry.USER_COLUMN_RATING, user.rating)
        values.put(DBContract.UserEntry.USER_COLUMN_LAST_MODIFIED, user.lastModified)
        values.put(DBContract.UserEntry.USER_COLUMN_DATE_CREATED, user.dateCreated)

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

        val check: Int = checkFollower(friend.SenderID, friend.ReceiverID)

        if (check == -1) {
            val db: SQLiteDatabase = writableDatabase

            // Create a new map of values, where column names are the keys
            val values = ContentValues()

            //values.put(DBContract.UserEntry.FOLLOWER_COLUMN_ID, friend.id)
            values.put(DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID, friend.SenderID)
            values.put(DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID, friend.ReceiverID)
            values.put(DBContract.UserEntry.FOLLOWER_COLUMN_DATE, friend.date)
            values.put(DBContract.UserEntry.FOLLOWER_COLUMN_TIME, friend.time)
            values.put(DBContract.UserEntry.FOLLOWER_COLUMN_CONDITION, friend.condition)

            db.insert(DBContract.UserEntry.FOLLOWER_TABLE_NAME, null, values)

            return true
        } else
            return false
    }

    @Throws(SQLiteConstraintException::class)
    fun setPassword(UserID: Int, newPassword: String): Boolean {

        val cv = ContentValues()
        cv.put(DBContract.UserEntry.USER_COLUMN_PASSWORD, newPassword)

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
        val selection = "${DBContract.UserEntry.USER_COLUMN_USER_ID} = ?"
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
                val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                val phone: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val password: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                val bioDescription: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_BIO_DESCRIPTION))
                val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                val rating: Float = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                val lastModified: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                val dateCreated: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                users = User(id, username, phone, password, fullName, bioDescription, email, rating, lastModified, dateCreated)
            }

        cursor.close()
        return users
    }

    fun getUsersData(userID: Int, columnName: String): String {
        val db: SQLiteDatabase = writableDatabase
        var pieceOfData = ""
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT $columnName FROM ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_USER_ID} = '$userID'", null)
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
            val line = "SELECT * FROM ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_USER_ID} = '$UserID'"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                if (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val phone: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val password: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                    val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                    val bioDescription: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_BIO_DESCRIPTION))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    val rating: Float = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                    val lastModified: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    val dateCreated: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                    users = User(id, username, phone, password, fullName, bioDescription, email, rating, lastModified, dateCreated)
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
            val line = "SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$UserID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '$num'"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            phoneNum,
                            "",
                            fullName,
                            "",
                            email,
                            -1.0F,
                            "",
                            ""
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

    fun readAllFollowing(SenderID: Int): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$SenderID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1'"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            phoneNum,
                            "",
                            fullName,
                            "",
                            email,
                            -1.0F,
                            "",
                            ""
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

    fun readAllMutualFollowers(myID: Int, userID: Int): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT * FROM (SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$myID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1') AS Q1 INNER JOIN " +
                    "(SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$userID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1') AS Q2 ON Q1.UserID = Q2.UserID"

            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val id: Int =
                        cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                    val username: String =
                        cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val phoneNum: String =
                        cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val fullName: String =
                        cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                    val email: String =
                        cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            phoneNum,
                            "",
                            fullName,
                            "",
                            email,
                            -1.0F,
                            "",
                            ""
                        )
                    )
                    cursor.moveToNext()
                }
            }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }
    }

    fun readFirstThreeMutualFollowers(myID: Int, userID: Int): ArrayList<String> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<String>()

        try {
            val line = "SELECT * FROM (SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$myID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1') AS Q1 INNER JOIN " +
                    "(SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER INNER JOIN ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} ON USER.UserID = ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID WHERE ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.ReceiverID = '$userID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1') AS Q2 ON Q1.Username = Q2.Username LIMIT 2"

            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val username: String =
                        cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    users.add(username)

                    cursor.moveToNext()
                }
            }

        } catch (e: SQLiteException) {
        } finally {
            cursor!!.close()
            db.close()
            return users
        }
    }

    fun searchAllUsers(except: Int, searchLine: String): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT UserID, Username, Phone, UserFullName, Email FROM USER WHERE UserID != '$except' AND (UserFirstName LIKE '$searchLine%' OR UserLastName LIKE '$searchLine%')"
            cursor = db.rawQuery(line, null)

            if (cursor.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                    val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                    val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                    val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(
                        User(
                            id,
                            username,
                            phoneNum,
                            "",
                            fullName,
                            "",
                            email,
                            0F,
                            "",
                            ""
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

    fun readFirstSixUsers(): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER LIMIT 6"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            db.close()
            cursor!!.close()
            return ArrayList()
        }

        if (cursor.moveToFirst())
            while (!cursor.isAfterLast) {
                val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(
                    User(
                        id,
                        username,
                        phoneNum,
                        "",
                        fullName,
                        "",
                        email,
                        0F,
                        "",
                        ""
                    )
                )
                cursor.moveToNext()
            }

        db.close()
        cursor!!.close()
        return users
    }

    fun readFirstTenUsers(searchLine: String): ArrayList<User> {
        val db: SQLiteDatabase = writableDatabase
        var cursor: Cursor? = null
        val users = ArrayList<User>()

        try {
            val line = "SELECT USER.UserID, USER.Username, USER.Phone, USER.UserFullName, USER.Email FROM USER LIMIT 10"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            db.close()
            cursor!!.close()
            return ArrayList()
        }

        if (cursor.moveToFirst())
            while (!cursor.isAfterLast) {
                val id: Int = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_ID))
                val username: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USERNAME))
                val phoneNum: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val fullName: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME))
                val email: String = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(
                    User(
                        id,
                        username,
                        phoneNum,
                        "",
                        fullName,
                        "",
                        email,
                        0F,
                        "",
                        ""
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
                    "AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1'", null)
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
                    "${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.SenderID = '$UserID' AND ${DBContract.UserEntry.FOLLOWER_TABLE_NAME}.Condition = '1'"
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
            val line = "SELECT * FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} WHERE FollowerID = (SELECT MAX(FriendshipID) FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME})"
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
        val selection = "${DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID} = ? AND ${DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID} = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$SenderID", "$ReceiverID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.FOLLOWER_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun checkFollower(SenderID: Int, ReceiverID: Int): Int {
        val db: SQLiteDatabase = writableDatabase
        val cursor: Cursor?
        var condition = -1

        try {
            val line = "SELECT Condition FROM ${DBContract.UserEntry.FOLLOWER_TABLE_NAME} WHERE SenderID = '$SenderID' AND ReceiverID = '$ReceiverID'"
            cursor = db.rawQuery(line, null)
            if (cursor.moveToFirst())
                condition = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return condition
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
                    DBContract.UserEntry.USER_COLUMN_USER_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_PHONE + " TEXT UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_USER_FULL_NAME + " TEXT, " +
                    DBContract.UserEntry.USER_COLUMN_BIO_DESCRIPTION + " TEXT, " +
                    DBContract.UserEntry.USER_COLUMN_EMAIL + " TEXT, " +
                    DBContract.UserEntry.USER_COLUMN_RATING + " NUMERIC NOT NULL DEFAULT 5.0, " +
                    DBContract.UserEntry.USER_COLUMN_LAST_MODIFIED + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_DATE_CREATED + " TEXT NOT NULL)"

        private const val SQL_CREATE_FOLLOWER_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.FOLLOWER_TABLE_NAME + " (" +
                    DBContract.UserEntry.FOLLOWER_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_TIME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FOLLOWER_COLUMN_CONDITION + " INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(" + DBContract.UserEntry.FOLLOWER_COLUMN_SENDER_ID + ") REFERENCES " +
                    DBContract.UserEntry.USER_TABLE_NAME + "(" + DBContract.UserEntry.USER_COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + DBContract.UserEntry.FOLLOWER_COLUMN_RECEIVER_ID + ") REFERENCES " +
                    DBContract.UserEntry.USER_TABLE_NAME + "(" + DBContract.UserEntry.USER_COLUMN_USER_ID + ") )"

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

        private const val SQL_CREATE_ATTENDEE_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.ATTENDEE_TABLE_NAME + " (" +
                    DBContract.UserEntry.ATTENDEE_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_EVENT_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_USER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_POSITION + " TEXT NOT NULL, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_CONDITION + " TEXT NOT NULL, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_LAST_MODIFIED + " TEXT NOT NULL, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_DATE_ACCEPTED + " TEXT, " +
                    DBContract.UserEntry.ATTENDEE_COLUMN_DATE_CREATED + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + DBContract.UserEntry.ATTENDEE_COLUMN_EVENT_ID + ") REFERENCES " +
                    DBContract.UserEntry.EVENT_TABLE_NAME + "(" + DBContract.UserEntry.EVENT_COLUMN_ID + "), " +
                    "FOREIGN KEY(" + DBContract.UserEntry.ATTENDEE_COLUMN_USER_ID + ") REFERENCES " +
                    DBContract.UserEntry.USER_TABLE_NAME + "(" + DBContract.UserEntry.USER_COLUMN_USER_ID + ") )"

        private const val SQL_DELETE_USER_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.USER_TABLE_NAME
        private const val SQL_DELETE_FOLLOWER_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.FOLLOWER_TABLE_NAME
        private const val SQL_DELETE_EVENT_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.EVENT_TABLE_NAME
        private const val SQL_DELETE_ATTENDEE_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.ATTENDEE_TABLE_NAME
    }

}