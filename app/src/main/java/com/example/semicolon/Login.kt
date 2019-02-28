package com.example.semicolon

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class Login : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //EditTexts
        val fEnter = findViewById<EditText>(R.id.fName)
        val sEnter = findViewById<EditText>(R.id.lName)

        //Buttons
        val logBut = findViewById<Button>(R.id.logBut)
        val forgotBut = findViewById<Button>(R.id.forgotBut)
        val createBut = findViewById<Button>(R.id.createBut)

        //TextViews
        val textView = findViewById<TextView>(R.id.textView)

        //"Sign In" listener
        logBut.setOnClickListener {
            val userName = fEnter.text.toString()
            val password = sEnter.text.toString()

            if (userName == "user") {
                if (password == "12345678") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else
                    Toast.makeText(this,"Wrong username or password", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this,"Wrong username", Toast.LENGTH_SHORT).show()

        }

        //"Recovery" listener
        forgotBut.setOnClickListener {
            val intent = Intent(this, DataRecovery::class.java)
            startActivity(intent)
        }

        //"Sign Up" listener
        createBut.setOnClickListener {
            val intent = Intent(this, FirstRegoList::class.java)
            startActivity(intent)
        }

        fEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val userName = fEnter.text.toString()
                val password = sEnter.text.toString()

                if (userName.length < 8)
                    fEnter.setTextColor(Color.RED)
                else
                    fEnter.setTextColor(Color.BLACK)
            }
        })

    }

}