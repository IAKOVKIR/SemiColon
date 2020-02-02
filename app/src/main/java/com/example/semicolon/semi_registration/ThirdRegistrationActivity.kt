package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.semicolon.Login
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper

class ThirdRegistrationActivity : Activity() {

    private var userArray: ArrayList<String>? = null
    private lateinit var db : DatabaseOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_third_activity)

        db = DatabaseOpenHelper(this)

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
                register()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Checkbox is not checked", Toast.LENGTH_LONG).show()
        }
    }
    /**
     * @function register() registers a user into the database based on their inputted information
     */
    private fun register() {
        // Get all input and make a new user
        db.insertUser(
            User("-1", userArray!![3], userArray!![6])
        )

        // Check user has been added to the database
        val user : User = db.findUserByUsernameAndPassword("-1", userArray!![3])
        if (user.id != -1) {
            // Successful with current code!
            Toast.makeText(this, "Successfully registered!", Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(this, "Could not register user.", Toast.LENGTH_LONG).show()
    }
}
