package com.example.semicolon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class second_rego_list : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_rego_list)

        val returnBut = findViewById<Button>(R.id.go_back)

        returnBut.setOnClickListener {
            onBackPressed()
        }

    }
}
