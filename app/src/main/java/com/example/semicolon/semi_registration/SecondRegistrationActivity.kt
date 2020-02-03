package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.semicolon.R

class SecondRegistrationActivity : Activity() {

    private lateinit var uEmail: EditText
    private lateinit var uPassword: EditText

    private var userName: String = ""
    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_second_activity)

        val backButton: ImageView = findViewById(R.id.back_button)
        val nextButton: Button = findViewById(R.id.next_button)

        uEmail= findViewById(R.id.email_field)
        uPassword = findViewById(R.id.password_field)

        userName = intent.getStringExtra("username")!!
        email = intent.getStringExtra("email")!!
        password = intent.getStringExtra("password")!!

        uEmail.setText(email)
        uPassword.setText(password)

        uEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = uEmail.text.toString()
            }
        })

        uPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = uPassword.text.toString()
            }
        })

        backButton.setOnClickListener{
            val intent = Intent(this, FirstRegistrationActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener{
            val intent = Intent(this, ThirdRegistrationActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
            finish()
        }
    }

}
