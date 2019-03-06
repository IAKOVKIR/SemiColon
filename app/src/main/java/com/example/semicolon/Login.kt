package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.CheckBox
import android.content.SharedPreferences



class Login : Activity() {

    var PREFS_NAME = "mypre"
    var PREF_USERNAME = "username"
    var PREF_PASSWORD = "password"

    //EditTexts
    var fEnter : EditText ?= null
    var sEnter : EditText ?= null
    var CheckLog : CheckBox ?= null

    public override fun onStart() {
        super.onStart()
        //read username and password from SharedPreferences
        getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //EditTexts
        fEnter = findViewById(R.id.fName)
        sEnter = findViewById(R.id.lName)

        //checkbox
        CheckLog = findViewById(R.id.checkLogin)

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
                /*val userName = fEnter!!.text.toString()

                if (userName.length < 8)
                    fEnter!!.setTextColor(Color.RED)
                else
                    fEnter!!.setTextColor(Color.BLACK)*/
            }
        })

    }

    fun doLogin() {
        val txtuser = fEnter!!.text.toString()
        val txtpwd = sEnter!!.text.toString()
        val username = "MrQuery"
        val password = "12345678"
        if (txtuser == username && txtpwd == password) {
            if (CheckLog!!.isChecked)
                rememberMe(username, password) //save username and password
            //show logout activity
            showLogout(username)

        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
        }


    }

    fun showLogout(username: String) {
        //display log out activity
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("user", username)
        startActivity(intent)
        finish()
    }

    fun getUser() {
        val pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val username = pref.getString(PREF_USERNAME, null)
        val password = pref.getString(PREF_PASSWORD, null)

        if (username != null || password != null) {
            //directly show logout form
            showLogout(username)
        }
    }

    fun rememberMe(user: String, password: String) {
        //save username and password in SharedPreferences
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_USERNAME, user)
            .putString(PREF_PASSWORD, password)
            .apply()
    }

}