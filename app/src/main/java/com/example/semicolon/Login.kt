package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.CheckBox
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class Login : Activity() {

    //variables (SharedPreferences)
    var prefName = "myPreferences"
    var prefUsername = "username"
    var prefPassword = "password"

    //EditTexts
    private var fEnter : EditText ?= null //username
    private var sEnter : EditText ?= null //password

    lateinit var usersDBHelper : DatabaseOpenHelper

    //CheckBox
    private var checkLog : CheckBox? = null //"remember me"

    public override fun onStart() {
        super.onStart()
        //reads username and password from SharedPreferences
        getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val strDate = sdf.format(c.time).trim()

        usersDBHelper = DatabaseOpenHelper(this)
        usersDBHelper.insertUser(User(1,"Chandra", "MrQuery", "0490506763", "12345678",
            "Melbourne", 1, 5.0F, "MrStealYourQuery@gmail.com"))
        usersDBHelper.insertUser(User(2,"Abbas", "Ice Wallow", "0490000000", "12345678",
            "Somewhere", 1, 3.2F, "BetterThanFather@gmail.com"))
        usersDBHelper.insertUser(User(3,"Carl", "Obama", "0490000001", "12345678",
            "Melbourne", 1, 5.0F, "CallMePresident@gmail.com"))
        usersDBHelper.insertRequest(Friend(1, 1, 2, strDate.substring(11, 19), strDate.substring(0, 10), "inProgress"))
        usersDBHelper.insertRequest(Friend(2, 1, 3, strDate.substring(11, 19), strDate.substring(0, 10), "inProgress"))
        usersDBHelper.insertRequest(Friend(3, 2, 3, strDate.substring(11, 19), strDate.substring(0, 10), "inProgress"))

        fEnter = findViewById(R.id.fName)
        sEnter = findViewById(R.id.lName)

        //checkbox
        checkLog = findViewById(R.id.checkLogin)

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
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }

        fEnter!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    /**
     * @function doLogin() retrieves data from two EditText's (txtUser - username, txtPassword - password)
     * If txtUser equals to username and txtPassword equals to password, then HomeActivity will be started
     * Else 'Toast' message will be displayed
     * If checkBox ("remember me") will be checked, then username and password will be saved in SharedPreferences
     */
    private fun doLogin() {
        val txtUser = fEnter!!.text.toString()
        val txtPassword = sEnter!!.text.toString()

        val list = usersDBHelper.readUser(txtUser, txtPassword)

        if (list.size > 0) {
            if (checkLog!!.isChecked)
            //save username and password in SharedPreferences
                rememberMe(list[0].phone, list[0].password)
            //show logout activity
            showHome(list[0])

        } else
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()

    }

    /**
     * @function showHome() starts HomeActivity and sends username
     */

    private fun showHome(list: User) {
        val intent = Intent(this, UserHomeActivity::class.java)
        val user = ArrayList<String>()
        user.add(list.id.toString())
        user.add(list.firstName)
        user.add(list.lastName)
        user.add(list.phone)
        user.add(list.password)
        user.add(list.city)
        user.add(list.rating.toString())
        user.add(list.email)
        intent.putStringArrayListExtra("user", user)
        startActivity(intent)
        finish()
    }

    /**
     * @function getUser() checks username and password.
     * If username or password contain something, then the logout activity will be opened directly
     */

    private fun getUser() {
        val pref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val username = pref.getString(prefUsername, "user") as String
        val password = pref.getString(prefPassword, "user") as String

        val list = usersDBHelper.readUser(username, password)

        if (list.size > 0)
            showHome(list[0])
    }

    /**
     * @function rememberMe() saves username and password values in SharedPreferences
     */

    //rememberMe() function saves username and password in SharedPreferences
    private fun rememberMe(user: String, password: String) {
        getSharedPreferences(prefName, Context.MODE_PRIVATE)
            .edit()
            .putString(prefUsername, user)
            .putString(prefPassword, password)
            .apply()
    }

}