package com.example.semicolon

import android.app.ActivityOptions
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstRegoList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_rego_list)

        val nextBut = findViewById<Button>(R.id.nextBut)

        nextBut.setOnClickListener {
            val intent = Intent(this, second_rego_list::class.java)
            val activityOption = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intent, activityOption.toBundle())
        }

    }
}
