package com.example.semicolon

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import java.util.Timer
import java.util.TimerTask

class WelcomeLabel : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_label)

        val timer = Timer()
        val intent = Intent(this, Login::class.java)

        timer.schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
                finish()
            }
        },  1400)

    }
}
