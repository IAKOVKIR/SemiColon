package com.example.semicolon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView

import java.util.Timer
import java.util.TimerTask

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer = Timer()
        val intent = Intent(this, Login::class.java)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val textSemiColon = findViewById<TextView>(R.id.label)
        textSemiColon.startAnimation(animFadeIn)

        timer.schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
                finish()
            }
        },  1000)

    }
}