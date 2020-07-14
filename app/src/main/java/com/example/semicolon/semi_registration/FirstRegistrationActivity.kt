package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.semicolon.login.LoginActivity
import com.example.semicolon.R

class FirstRegistrationActivity : Activity() {

    private lateinit var username: EditText

    private var userName: String? = ""
    private var email: String? = ""
    private var password: String? = ""

    private var valid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_first_activity)

        username = findViewById(R.id.username)

        val backButton: ImageView = findViewById(R.id.back_button)
        val nextButton: Button = findViewById(R.id.next_button)

        userName = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")

        username.setText(userName)

        changeListener(username)

        nextButton.setOnClickListener{
            valid = validate(0)

            if (valid) {
                val intent = Intent(this, SecondRegistrationActivity::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            }
            else {
                val shake: Animation = AnimationUtils.loadAnimation(
                    this@FirstRegistrationActivity,
                    R.anim.editext_shaker)

                username.startAnimation(shake)

                //Toast.makeText(this, "Insufficient information inputted", Toast.LENGTH_LONG).show()
            }
        }

        backButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validate(num: Int): Boolean {
        // validation code can and should be changed
        return when (num) {
            0 -> username.text.isNotEmpty()
            else -> false
        }
    }

    private fun changeListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                if (!validate(num)) {
//                    editText.backgroundTintList = ColorStateList.valueOf(Color.RED)
//                }
//                else {
//                    editText.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
//                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userName = editText.text.toString()
            }
        })
    }

}
