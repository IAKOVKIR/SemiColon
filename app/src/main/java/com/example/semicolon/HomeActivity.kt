package com.example.semicolon

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView

class HomeActivity : Activity() {

    private var user: String? = null
    var log = Login()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //get username sent from the log in activity
        val intent = intent
        val b = intent.extras
        user = b!!.getString("user")
    }

    public override fun onStart() {
        super.onStart()
        val view = findViewById<TextView>(R.id.UserName)
        view.text = "Welcome " + user!!
    }


    fun logOut(view: View) {
        val sharedPrefs = getSharedPreferences(log.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
        user = ""
        //show login form
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

}