package com.example.semicolon.sqlite_database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.semicolon.DBContract
import com.example.semicolon.Friend
import com.example.semicolon.User

import java.util.ArrayList

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, DATABASE_VERSION) {

    // Gets the data repository in write mode
    private val db: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER_TABLE)
        db.execSQL(SQL_CREATE_FRIEND_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_FRIEND_TABLE)
        db.execSQL(SQL_DELETE_USER_TABLE)
        onCreate(db)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: User): Boolean {

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
    }

    @Throws(SQLiteConstraintException::class)
    fun insertRequest(friend: Friend): Boolean {

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
        val cv = ContentValues()

        cv.put("Password", newPassword)

        //updates the record in USER table
        db.update(DBContract.UserEntry.USER_TABLE_NAME, cv, "UserID = $UserID", null)

        //alternative way
        //db.execSQL("UPDATE USER SET Password = '$newPassword' WHERE UserID = '$UserID'")

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun updateRequest(SenderID: Int, ReceiverID: Int, condition: Int): Boolean {
        val cv = ContentValues()
        cv.put("Condition", condition)

        db.update(DBContract.UserEntry.FRIEND_TABLE_NAME, cv, "SenderID = $SenderID AND ReceiverID = $ReceiverID", null)

        //alternative way
        //db.execSQL("UPDATE USER SET Password = '$newPassword' WHERE UserID = '$UserID'")

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(UserID: Int): Boolean {
        // Define 'where' part of query.
        val selection: String = DBContract.UserEntry.USER_COLUMN_ID + " = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$UserID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.USER_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun findUserByPhoneAndPassword(Phone: String, Password: String): User {
        var users = User(-1, "-1", "-1",
            "-1", "-1", "-1",
            0, -1.0F, "-1")
        val cursor: Cursor

        try {
            cursor = db.rawQuery("select * from ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_PHONE} = '$Phone' AND ${DBContract.UserEntry.USER_COLUMN_PASSWORD} = '$Password'", null)
        } catch (e: SQLiteException) {
            return users
        }

        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val password = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                val agreementCheck = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK)).toByte()
                val rating = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                users = User(id, firstName, lastName,
                        phone, password, city,
                        agreementCheck, rating, email)

            }
        }
        cursor.close()
        return users
    }

    fun findUserByID(UserID: Int): User {
        var users = User(-1, "-1", "-1",
            "-1", "-1", "-1",
            0, -1.0F, "-1")
        val cursor: Cursor

        try {
            cursor = db.rawQuery("select * from ${DBContract.UserEntry.USER_TABLE_NAME} WHERE ${DBContract.UserEntry.USER_COLUMN_ID} = '$UserID'", null)
        } catch (e: SQLiteException) {
            return users
        }

        if (cursor.moveToFirst()) {
            if (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val password = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PASSWORD))
                val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                val agreementCheck = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK)).toByte()
                val rating = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_RATING)).toFloat()
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))

                users = User(id, firstName, lastName,
                    phone, password, city,
                    agreementCheck, rating, email)

            }
        }
        cursor.close()
        return users
    }

    fun readAllFollowers(UserID: Int, num: Int, except: Int): ArrayList<User> {
        val users = ArrayList<User>()
        var cursor: Cursor? = null

        try {
            val line = "SELECT USER.UserID, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.City, USER.Email FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.SenderID WHERE FRIEND.ReceiverID = '$UserID' AND FRIEND.Condition = '$num' AND FRIEND.SenderID != '$except'"
            cursor = db.rawQuery(line, null)
            if (cursor!!.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                    val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                    cursor.moveToNext()
                }
        } catch (e: SQLiteException) {
        } finally {
            cursor?.close()
            db.close()
            return users
        }
    }

    fun readAllFollowing(UserID: Int): ArrayList<User> {
        val users = ArrayList<User>()
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT USER.UserID, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.City, USER.Email FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.ReceiverID WHERE FRIEND.SenderID = '$UserID' AND FRIEND.Condition = '-1'", null)
            if (cursor!!.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                    val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                    cursor.moveToNext()
                }
        } catch (e: SQLiteException) {
        } finally {
            cursor?.close()
            db.close()
            return users
        }
    }

    fun searchAllUsers(except: Int, searchLine: String): ArrayList<User> {
        val users = ArrayList<User>()
        var cursor: Cursor? = null

        try {
            val line = "SELECT UserID, UserFirstName, UserLastName, Phone, City, Email FROM USER WHERE UserID != '$except' AND (UserFirstName LIKE '$searchLine%' OR UserLastName LIKE '$searchLine%')"
            cursor = db.rawQuery(line, null)

            if (cursor!!.moveToFirst())
                while (!cursor.isAfterLast) {
                    val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                    val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                    val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                    val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                    val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                    val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                    users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                    cursor.moveToNext()
                }

        } catch (e: SQLiteException) {
        } finally {
            cursor?.close()
            db.close()
            return users
        }

    }

    fun searchAllFollowing(UserID: Int, searchLine: String): ArrayList<User> {
        val users = ArrayList<User>()
        var cursor: Cursor? = null

        try {
            val line = "SELECT USER.UserID, USER.UserFirstName, USER.UserLastName, USER.Phone, USER.City, USER.Email FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.ReceiverID WHERE FRIEND.SenderID = '$UserID' AND FRIEND.Condition = '-1' AND (USER.UserFirstName LIKE '$searchLine%' OR USER.UserLastName LIKE '$searchLine%')"
            cursor = db.rawQuery(line, null)
        } catch (e: SQLiteException) {
            db.close()
            cursor!!.close()
            return ArrayList()
        }

        if (cursor!!.moveToFirst())
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                cursor.moveToNext()
            }

        db.close()
        cursor.close()
        return users
    }

    fun countFollowers(UserID: Int): Int {
        var total = 0
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.SenderID WHERE FRIEND.ReceiverID = '$UserID' AND FRIEND.Condition = '-1'", null)
            if (cursor!!.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
        } finally {
            cursor?.close()
            return total
        }
    }

    fun countFollowing(UserID: Int): Int {
        var total = 0
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM USER INNER JOIN FRIEND ON USER.UserID = FRIEND.ReceiverID WHERE FRIEND.SenderID = '$UserID' AND FRIEND.Condition = '-1'", null)
            if (cursor!!.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
        } finally {
            cursor?.close()
            db.close()
            return total
        }
    }

    fun countFriendTable(): Int {
        var total = 0
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM FRIEND WHERE FriendshipID = (SELECT MAX(FriendshipID) FROM FRIEND)", null)
            if (cursor!!.moveToFirst())
                total = cursor.getInt(0)
        } catch (e: SQLiteException) {
            return 0
        }

        cursor.close()
        return total + 1
    }

    fun deleteFollowing(SenderID: Int, ReceiverID: Int): Boolean {
        // Define 'where' part of query.
        val selection: String = DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " = ? AND " + DBContract.UserEntry.FRIEND_COLUMN_RECEIVER_ID + " = ?"
        // Specify arguments in placeholder order.
        val selectionArgs: Array<String> = arrayOf("$SenderID", "$ReceiverID")
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.FRIEND_TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun checkFollower(SenderID: Int, ReceiverID: Int): Int {
        var condition = 0
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT Condition FROM FRIEND WHERE SenderID = '$SenderID' AND ReceiverID = '$ReceiverID'", null)
            if (cursor!!.moveToFirst())
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
                    DBContract.UserEntry.USER_COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_PHONE + " TEXT NOT NULL UNIQUE, " +
                    DBContract.UserEntry.USER_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_CITY + " TEXT NOT NULL, " +
                    DBContract.UserEntry.USER_COLUMN_AGREEMENT_CHECK + " NUMERIC NOT NULL DEFAULT 1, " +
                    DBContract.UserEntry.USER_COLUMN_RATING + " NUMERIC NOT NULL DEFAULT 5.0, " +
                    DBContract.UserEntry.USER_COLUMN_EMAIL + " TEXT)"

        private const val SQL_CREATE_FRIEND_TABLE: String =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.FRIEND_TABLE_NAME + " (" +
                    DBContract.UserEntry.FRIEND_COLUMN_FRIEND_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " INTEGER NOT NULL CONSTRAINT " +
                    DBContract.UserEntry.FRIEND_COLUMN_SENDER_ID + " REFERENCES " + DBContract.UserEntry.USER_TABLE_NAME + ", " +
                    DBContract.UserEntry.FRIEND_COLUMN_RECEIVER_ID + " INTEGER NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_DATE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_TIME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.FRIEND_COLUMN_CONDITION + " INTEGER NOT NULL DEFAULT 1)"

        private const val SQL_DELETE_USER_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.USER_TABLE_NAME
        private const val SQL_DELETE_FRIEND_TABLE: String = "DROP TABLE IF EXISTS " + DBContract.UserEntry.FRIEND_TABLE_NAME
    }

    //in development stage

    /*fun searchAllFollowers(UserID: String, num: Int, except: Int, searchLine: String): ArrayList<User> {
        val users = ArrayList<User>()
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

        if (cursor!!.moveToFirst())
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_LAST_NAME))
                val phoneNum = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_PHONE))
                val city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_CITY))
                val email = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.USER_COLUMN_EMAIL))
                users.add(User(id, firstName, lastName, phoneNum, "", city, 1, 0F, email))
                cursor.moveToNext()
            }

        cursor.close()
        return users
    }*/

}