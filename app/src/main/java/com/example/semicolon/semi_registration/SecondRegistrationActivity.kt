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
import android.widget.Toast
import com.example.semicolon.R

class SecondRegistrationActivity : Activity() {

    private lateinit var uEmail: EditText
    private lateinit var uPassword: EditText

    private lateinit var uPhoto: ImageView

    private var userName: String? = ""
    private var email: String? = ""
    private var password: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_second_activity)

        val backButton: ImageView = findViewById(R.id.back_button)
        val nextButton: Button = findViewById(R.id.next_button)

        uEmail= findViewById(R.id.email_field)
        uPassword = findViewById(R.id.password_field)
        uPhoto = findViewById(R.id.log_image)

        userName = intent.getStringExtra("username")
        email = intent.getStringExtra("email")
        password = intent.getStringExtra("password")

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

        uPhoto.setOnClickListener{
            Toast.makeText(this, "Logo pressed", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener{
            val intent = Intent(this, FirstRegistrationActivity::class.java)
            intent.putExtra("username", userName)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener{
            if (validate(0) && validate(1)) {
                val intent = Intent(this, ThirdRegistrationActivity::class.java)
                intent.putExtra("username", userName)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
                finish()
            }
            else {
                val shake: Animation = AnimationUtils.loadAnimation(
                    this@SecondRegistrationActivity,
                    R.anim.editext_shaker)

                uEmail.startAnimation(shake)
                uPassword.startAnimation(shake)
            }
        }
    }

    private fun validate(num: Int): Boolean {
        return when (num) {
            0 -> {
                // regex pattern obtained from: https://www.roytuts.com/validate-email-address-with-regular-expression-using-kotlin/
                val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
                emailRegex.toRegex().matches(uEmail.text.toString())
            }

            1 -> {
                uPassword.text.isNotEmpty()
            }

            else -> {
                false
            }
        }
    }

}
