package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Login : Activity() {

    //variables (SharedPreferences)
    var prefName = "myPreferences"
    var prefVar = arrayOf("id", "firstName", "lastName", "phone", "password", "city", "rating", "email")

    //EditTexts
    private lateinit var fEnter : EditText //username
    private lateinit var sEnter : EditText //password

    private lateinit var usersDBHelper : DatabaseOpenHelper

    public override fun onStart() {
        super.onStart()
        //reads username and password from SharedPreferences
        getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usersDBHelper = DatabaseOpenHelper(this)

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        usersDBHelper.insertUser(User(1,"Chandra", "MrQuery", "0490506763", "12345678",
            "Melbourne", 1, 5.0F, "MrStealYourQuery@gmail.com"))
        usersDBHelper.insertUser(User(2,"Abbas", "Ice Wallow", "0490000000", "12345678",
            "Somewhere", 1, 3.2F, "BetterThanFather@gmail.com"))
        usersDBHelper.insertUser(User(3,"Carl", "Obama", "0490000001", "12345678",
            "Melbourne", 1, 5.0F, "CallMePresident@gmail.com"))
        usersDBHelper.insertUser(User(4,"Matt", "Merin", "0490000002", "12345678",
            "Melbourne", 1, 5.0F, "MattMerinOnTheBeat@gmail.com"))

        usersDBHelper.insertRequest(Friend(1, 1, 2, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(2, 1, 3, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(3, 2, 3, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(4, 3, 1, strDate.substring(11, 19), strDate.substring(0, 10), 1))
        usersDBHelper.insertRequest(Friend(5, 1, 4, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(6, 2, 4, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(7, 3, 4, strDate.substring(11, 19), strDate.substring(0, 10), 3))
        usersDBHelper.insertRequest(Friend(8, 4, 3, strDate.substring(11, 19), strDate.substring(0, 10), 3))

        fEnter = findViewById(R.id.userName)
        sEnter = findViewById(R.id.password)

        //Buttons
        val logBut = findViewById<Button>(R.id.logBut)
        val forgotBut = findViewById<Button>(R.id.forgotBut)
        val createBut = findViewById<Button>(R.id.createBut)

        //"Sign In" listener
        logBut.setOnClickListener {
            doLogin()
        }

        //"Recovery" listener
        forgotBut.setOnClickListener {
            val intent = Intent(this, DataRecovery::class.java)
            startActivity(intent)
        }

        //"Sign Up" listener
        createBut.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

    }

    /**
     * @function doLogin() retrieves data from two EditText's (txtPhone - phone number, txtPassword - password)
     * If txtPhone equals to username and txtPassword equals to password, then HomeActivity will be started
     * Else 'Toast' message will be displayed
     * If checkBox ("remember me") will be checked, then username and password will be saved in SharedPreferences
     */
    private fun doLogin() {
        val txtPhone = fEnter.text.toString()
        val txtPassword = sEnter.text.toString()

        val list = usersDBHelper.readUser(txtPhone, txtPassword)

        if (list.size > 0) {
            //save username and password in SharedPreferences
            rememberMe(list[0])
            //show logout activity
            showHome()
        } else
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()

    }

    /**
     * @function showHome() starts HomeActivity and sends username
     */

    private fun showHome() {
        val intent = Intent(this, UserHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @function getUser() checks username and password.
     * If username or password contain something, then the logout activity will be opened directly
     */

    private fun getUser() {
        val pref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val username = pref.getString(prefVar[3], "") as String
        val password = pref.getString(prefVar[4], "") as String

        val list = usersDBHelper.readUser(username, password)

        if (list.size > 0)
            showHome()
    }

    /**
     * @function rememberMe() saves username and password values in SharedPreferences
     */

    //rememberMe() function saves username and password in SharedPreferences
    private fun rememberMe(user: User) {
        getSharedPreferences(prefName, Context.MODE_PRIVATE)
            .edit()
            .putString(prefVar[0], user.id.toString())
            .putString(prefVar[1], user.firstName)
            .putString(prefVar[2], user.lastName)
            .putString(prefVar[3], user.phone)
            .putString(prefVar[4], user.password)
            .putString(prefVar[5], user.city)
            .putString(prefVar[6], user.rating.toString())
            .putString(prefVar[7], user.email)
            .apply()
    }

}