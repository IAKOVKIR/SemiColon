package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper

class ThirdRegistrationActivity : Activity() {

    private var userName: String? = ""
    private var email: String? = ""
    private var password: String? = ""
    private lateinit var db : DatabaseOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_third_activity)

        db = DatabaseOpenHelper(this)

        val backButton: ImageView = findViewById(R.id.back_button)
        val userNameText: TextView = findViewById(R.id.userNameText)

        userName = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")

        userNameText.setText("> ${userName.toString()}")

        register()

        backButton.setOnClickListener{
            val intent = Intent(this, SecondRegistrationActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
            finish()
        }

//        finishButton.setOnClickListener{
//            if (checkBox.isChecked) {
//                register()
//                val intent = Intent(this, Login::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finish()
//            } else
//                Toast.makeText(this, "Checkbox is not checked", Toast.LENGTH_LONG).show()
//        }
    }
    /**
     * @function register() registers a user into the database based on their inputted information
     */
    private fun register() {
        // Get all input and make a new user
        db.insertUser(
            User(userName!!, password!!, email!!)
        )

        // Check user has been added to the database
        val user : User = db.findUserByUsernameAndPassword(userName!!, password!!)
        if (user.id != -1) {
            // Successful with current code!
            Toast.makeText(this, "Successfully registered!", Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(this, "Could not register user.", Toast.LENGTH_LONG).show()
    }
}
