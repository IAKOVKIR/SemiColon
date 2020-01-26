package com.example.semicolon.semi_registration

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
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

    //Buttons
    lateinit var buttonNext : Button
    lateinit var buttonBack : Button

    lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_registration)

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