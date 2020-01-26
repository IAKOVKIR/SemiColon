package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.example.semicolon.R

class SecondRegistrationActivity : Activity() {

    private lateinit var uCity: EditText
    private lateinit var uCountry: EditText
    private lateinit var uEmail: EditText

    private var userArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_second_activity)

        val backButton: Button = findViewById(R.id.buttonBack)
        val nextButton: Button = findViewById(R.id.buttonNext)

        uCity = findViewById(R.id.city)
        uCountry = findViewById(R.id.country)
        uEmail= findViewById(R.id.email)

        userArray = intent.getStringArrayListExtra("user_array")

        uCity.setText(userArray!![4])
        uCountry.setText(userArray!![5])
        uEmail.setText(userArray!![6])

        changeListener(uCity, 4)
        changeListener(uCountry, 5)
        changeListener(uEmail, 6)

        backButton.setOnClickListener{
            val intent = Intent(this, FirstRegistrationActivity::class.java)
            intent.putStringArrayListExtra("user_array",userArray!!)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener{
            val intent = Intent(this, ThirdRegistrationActivity::class.java)
            intent.putStringArrayListExtra("user_array",userArray!!)
            startActivity(intent)
            finish()
        }
    }

    private fun changeListener(editText: EditText, num: Int) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userArray!![num] = editText.text.toString()
            }
        })
    }

}
