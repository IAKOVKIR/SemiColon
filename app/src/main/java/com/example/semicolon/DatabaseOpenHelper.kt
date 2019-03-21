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
        db.execSQL(SQL_CREATE_ENTRIES)
        Log.d("CREATING TABLE :", "SUCCESS")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.d("UPGRADING TABLE :", "SUCCESS")
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: User): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_USER_ID, user.id)
        values.put(DBContract.UserEntry.COLUMN_FIRST_NAME, user.firstName)
        values.put(DBContract.UserEntry.COLUMN_LAST_NAME, user.lastName)
        values.put(DBContract.UserEntry.COLUMN_PHONE, user.phone)
        values.put(DBContract.UserEntry.COLUMN_PASSWORD, user.password)
        values.put(DBContract.UserEntry.COLUMN_CITY, user.city)
        values.put(DBContract.UserEntry.COLUMN_AGREEMENT_CHECK, user.agreementCheck)
        values.put(DBContract.UserEntry.COLUMN_RATING, user.rating)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(UserID: String): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.UserEntry.COLUMN_USER_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(UserID)
        // Issue SQL statement.
        db.delete(DBContract.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readUser(Phone: String, Password: String): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        val cursor: Cursor
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_PHONE + " = \"" + Phone + "\" AND " + DBContract.UserEntry.COLUMN_PASSWORD + " = " +
                    "\"" + Password + "\"", null)
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

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_USER_ID))
                firstName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_LAST_NAME))
                phone = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PHONE))
                password = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_PASSWORD))
                city = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_CITY))
                agreementCheck = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_AGREEMENT_CHECK)).toByte()
                rating = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_RATING)).toFloat()

                users.add(User(id, firstName, lastName, phone, password, city, agreementCheck, rating))
                cursor.moveToNext()
            }
        }
        return users
    }

    /*fun readAllUsers(): ArrayList<User> {
        val users = ArrayList<User>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var userid: String
        var name: String
        var age: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                userid = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_USER_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_NAME))
                age = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_AGE))

                users.add(User(userid, name, age))
                cursor.moveToNext()
            }
        }
        return users
    }*/

    companion object {
        // If you change the database schema, you must increment the database version.
        var DATABASE_VERSION = 1
        const val DATABASE_NAME = "SemiColon"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DBContract.UserEntry.TABLE_NAME + " (" +
                    DBContract.UserEntry.COLUMN_USER_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    DBContract.UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_PHONE + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                    DBContract.UserEntry.COLUMN_AGREEMENT_CHECK + " NUMERIC NOT NULL DEFAULT 1, " +
                    DBContract.UserEntry.COLUMN_RATING + " NUMERIC NOT NULL DEFAULT 5.0)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME
    }

}