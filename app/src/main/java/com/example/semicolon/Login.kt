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

class Login : Activity() {

    //variables (SharedPreferences)
    var prefName = "myPreferences"
    var prefUsername = "username"
    var prefPassword = "password"

    //EditTexts
    private var fEnter : EditText ?= null //username
    private var sEnter : EditText ?= null //password

    //CheckBox
    private var checkLog : CheckBox ?= null //"remember me"

    public override fun onStart() {
        super.onStart()
        //reads username and password from SharedPreferences
        getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        val username = "MrQuery"
        val password = "12345678"
        if (txtUser == username && txtPassword == password) {
            if (checkLog!!.isChecked)
            //save username and password in  SharedPreferences
                rememberMe(username, password)
            //show logout activity
            showLogout(username)

        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
        }


    }

    /**
     * @function showLogout() starts HomeActivity and sends username
     */

    private fun showLogout(username: String) {
        val intent = Intent(this, UserHomeActivity::class.java)
        intent.putExtra("user", "Chandra")
        intent.putExtra("user1", username)
        startActivity(intent)
        finish()
    }

    /**
     * @function getUser() checks username and password.
     * If username or password contain something, then the logout activity will be opened directly
     */

    private fun getUser() {
        val pref = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val username = pref.getString(prefUsername, null)
        val password = pref.getString(prefPassword, null)

        if (username != null || password != null)
            showLogout(username!!.toString())
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