package com.example.semicolon.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.semicolon.R

import java.util.Timer
import java.util.TimerTask

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer = Timer()
        val intent = Intent(this, LoginActivity::class.java)
        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val finalAnimation = AnimationSet(true)
        val textSemiColon: TextView = findViewById(R.id.label)

        finalAnimation.addAnimation(animFadeIn)
        finalAnimation.addAnimation(animFadeOut)

        textSemiColon.startAnimation(finalAnimation)
        timer.schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
                finish()
            }
        },  2000)

    }
}