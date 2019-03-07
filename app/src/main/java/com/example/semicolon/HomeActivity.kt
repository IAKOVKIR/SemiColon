package com.example.semicolon

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.TextView

class HomeActivity : Activity() {

    private var user: String? = null
    private var log = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        //get username sent from the log in activity
        val intent = intent
        val b = intent.extras
        user = b!!.getString("user")
    }

    /**
     * @function onStart() displays text on TextView
     */

    public override fun onStart() {
        super.onStart()
        val view = findViewById<TextView>(R.id.UserName)
        view.text = "welcome $user"
    }

    /**
     * @function logOut() removes saved username and password from SharedPreferences and
     * returns to login form
     */

    fun logOut() {
        val sharedPrefs = getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
        user = ""

        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

}