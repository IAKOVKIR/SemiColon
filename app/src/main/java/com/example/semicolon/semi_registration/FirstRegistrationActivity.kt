package com.example.semicolon.semi_registration

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.semicolon.Login
import com.example.semicolon.R

class FirstRegistrationActivity : Activity() {

    private lateinit var fName: EditText
    private lateinit var lName: EditText
    private lateinit var pNumber: EditText
    lateinit var password: EditText
    private lateinit var cPassword: EditText

    private var userArray: ArrayList<String>? = null
    private var valid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_first_activity)

        fName = findViewById(R.id.first_name)
        lName = findViewById(R.id.last_name)
        pNumber = findViewById(R.id.phone_number)
        password = findViewById(R.id.password)
        cPassword = findViewById(R.id.confirm_password)
        val backButton: ImageView = findViewById(R.id.back_button)
        val nextButton: Button = findViewById(R.id.buttonNext)

        userArray = intent.getStringArrayListExtra("user_array")

        if (userArray.isNullOrEmpty())
            userArray = arrayListOf("", "", "", "", "", "", "", "")
        else {
            fName.setText(userArray!![0])
            lName.setText(userArray!![1])
            pNumber.setText(userArray!![2])
            password.setText(userArray!![3])
        }

        changeListener(fName, 0)
        changeListener(lName, 1)
        changeListener(pNumber, 2)
        changeListener(password, 3)
        changeListener(cPassword, 4)

        nextButton.setOnClickListener{
            valid = validate(0) && validate(1) && validate(2) && validate(3)
                    && validate(4)
            if (valid) {
                val intent = Intent(this, SecondRegistrationActivity::class.java)
                intent.putStringArrayListExtra("user_array", userArray)
                startActivity(intent)
                finish()
            } else
                Toast.makeText(this, "Insufficient information inputted", Toast.LENGTH_LONG).show()
        }

        backButton.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validate(num: Int): Boolean {
        // validation code can and should be changed
        when (num) {
            0 -> {
                return fName.text.isNotEmpty()
            }

            1 -> {
                return lName.text.isNotEmpty()
            }

            2 -> {
                return pNumber.text.length == 10 && pNumber.text.isDigitsOnly()
            }

            3 -> {
                return password.text.isNotEmpty()
            }

            4 -> {
                return cPassword.text.toString() == password.text.toString()
            }

            else -> {
                return false
            }
        }
    }

    private fun changeListener(editText: EditText, num: Int) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!validate(num)) {
                    editText.backgroundTintList = ColorStateList.valueOf(Color.RED)
                }
                else {
                    editText.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userArray!![num] = editText.text.toString()
            }
        })
    }

}
