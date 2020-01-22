package com.example.semicolon.semi_registration

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.google.android.material.textfield.TextInputEditText

class Registration : Activity() {

    private lateinit var mSlideViewPager : ViewPager

    //TextViews (Three circles on navigation)
    lateinit var firstCircle : TextView
    lateinit var secondCircle : TextView
    lateinit var thirdCircle : TextView

    //EditTexts
    private lateinit var firstName : TextInputEditText
    private lateinit var lastName : TextInputEditText
    private lateinit var phone : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var confirmPassword : TextInputEditText
    private lateinit var city : TextInputEditText
    private lateinit var country : TextInputEditText
    lateinit var email : TextInputEditText

    //CheckBoxes
    private lateinit var agreementBox : CheckBox

    //db helper
    private lateinit var db : DatabaseOpenHelper

    //LinearLayout (for date)
    // do something with this later

    //Buttons
    lateinit var buttonNext : Button
    lateinit var buttonBack : Button

    lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_registration)

        db = DatabaseOpenHelper(this)

        val sliderAdapter = SliderAdapter(this)

        mSlideViewPager = findViewById(R.id.slideViewPager)

        buttonBack = findViewById(R.id.buttonBack)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener {
            mSlideViewPager.currentItem = 1
        }

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        firstCircle = findViewById(R.id.FirstCircle)
        secondCircle = findViewById(R.id.SecondCircle)
        thirdCircle = findViewById(R.id.ThirdCircle)

        mSlideViewPager.adapter = sliderAdapter
        mSlideViewPager.addOnPageChangeListener(pageChangeListener)

    }

    /**
     * @function Boolean.toByte() is an extension function of the Boolean class used to convert a Boolean to a Byte
     */
    private fun Boolean.toByte() : Byte = if (this) 1 else 0

    /**
     * @function register() registers a user into the database based on their inputted information
     */
    private fun register() {
        // Get all input and make a new user
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        phone = findViewById(R.id.phone_number)
        password = findViewById(R.id.password)
        city = findViewById(R.id.city)
        country = findViewById(R.id.country)
        email = findViewById(R.id.email)
        agreementBox = findViewById(R.id.checkBox)

        db.insertUser(User(52, firstName.text.toString(), lastName.text.toString(),
            phone.text.toString(), password.text.toString(), city.text.toString(), agreementBox.isChecked.toByte(),
            5.0F, email.text.toString()))

        // Check user has been added to the database
        val user : User = db.findUserByPhoneAndPassword("0456733245", "12345678")
        if (user.id != -1) {
            Toast.makeText(this, "Successfully registered!", Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(this, "Could not register user", Toast.LENGTH_LONG).show()
    }

    private var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            val wordNext: String = getString(R.string.word_next)
            val wordBack: String = getString(R.string.word_back)
            val grayColor: Int = Color.GRAY
            val whiteColor: Int = Color.WHITE

            when (position) {
                0 -> {
                    firstCircle.setTextColor(whiteColor)
                    secondCircle.setTextColor(grayColor)
                    thirdCircle.setTextColor(grayColor)

                    buttonBack.isEnabled = false
                    buttonBack.text = ""
                    buttonNext.text = wordNext

                    buttonNext.setOnClickListener {
                        mSlideViewPager.currentItem = 1
                    }

                }
                1 -> {
                    secondCircle.setTextColor(whiteColor)
                    firstCircle.setTextColor(grayColor)
                    thirdCircle.setTextColor(grayColor)

                    buttonBack.isEnabled = true
                    buttonBack.text = wordBack
                    buttonNext.text = wordNext

                    buttonNext.setOnClickListener {
                        mSlideViewPager.currentItem = 2
                    }

                    buttonBack.setOnClickListener {
                        mSlideViewPager.currentItem = 0
                    }

                }
                else -> {

                    var view: View? = currentFocus
                    if (view == null)
                        view = View(applicationContext)

                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    thirdCircle.setTextColor(whiteColor)
                    firstCircle.setTextColor(grayColor)
                    secondCircle.setTextColor(grayColor)

                    buttonBack.isEnabled = true
                    buttonBack.text = wordBack
                    buttonNext.text = getString(R.string.word_finish)

                    buttonNext.setOnClickListener {
                        register()
                        finish()
                    }

                    buttonBack.setOnClickListener {
                        mSlideViewPager.currentItem = 1
                    }

                }
            }

        }
    }
}