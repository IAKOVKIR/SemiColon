package com.example.semicolon.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.semicolon.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //hides the AppCompatActivity bar
        supportActionBar!!.hide()
    }

}