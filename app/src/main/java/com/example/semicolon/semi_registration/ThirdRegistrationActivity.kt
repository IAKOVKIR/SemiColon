package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.semicolon.Login
import com.example.semicolon.R

class ThirdRegistrationActivity : Activity() {

    private var userArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_third_activity)

        val backButton: Button = findViewById(R.id.buttonBack)
        val finishButton: Button = findViewById(R.id.buttonFinish)
        val checkBox: CheckBox = findViewById(R.id.checkBox)

        userArray = intent.getStringArrayListExtra("user_array")

        backButton.setOnClickListener{
            val intent = Intent(this, SecondRegistrationActivity::class.java)
            intent.putStringArrayListExtra("user_array",userArray!!)
            startActivity(intent)
            finish()
        }

        finishButton.setOnClickListener{
            if (checkBox.isChecked) {
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Checkbox is not checked", Toast.LENGTH_LONG).show()
        }
    }
}
