package com.example.semicolon.semi_registration

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.view.View
import com.example.semicolon.R

class Registration : Activity() {

    private var mSlideViewPager : ViewPager? = null

    //TextViews (Three circles on navigation)
    var firstCircle : TextView? = null
    var secondCircle : TextView? = null
    var thirdCircle : TextView? = null

    //Buttons
    var buttonNext : Button? = null
    var buttonBack : Button? = null

    var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_registration)

        val sliderAdapter = SliderAdapter(this)

        mSlideViewPager = findViewById(R.id.slideViewPager)

        buttonBack = findViewById(R.id.buttonBack)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext!!.setOnClickListener {
            mSlideViewPager!!.currentItem = 1
        }

        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        firstCircle = findViewById(R.id.FirstCircle)
        secondCircle = findViewById(R.id.SecondCircle)
        thirdCircle = findViewById(R.id.ThirdCircle)

        mSlideViewPager?.adapter = sliderAdapter
        mSlideViewPager!!.addOnPageChangeListener(pageChangeListener)

    }

    private var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            when (position) {
                0 -> {
                    firstCircle!!.setTextColor(Color.WHITE)
                    secondCircle!!.setTextColor(Color.GRAY)
                    thirdCircle!!.setTextColor(Color.GRAY)

                    buttonBack!!.isEnabled = false
                    buttonBack!!.text = ""
                    buttonNext!!.text = getString(R.string.word_next)

                    buttonNext!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 1
                    }

                }
                1 -> {
                    secondCircle!!.setTextColor(Color.WHITE)
                    firstCircle!!.setTextColor(Color.GRAY)
                    thirdCircle!!.setTextColor(Color.GRAY)

                    buttonBack!!.isEnabled = true
                    buttonBack!!.text = getString(R.string.word_back)
                    buttonNext!!.text = getString(R.string.word_next)

                    buttonNext!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 2
                    }

                    buttonBack!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 0
                    }

                }
                else -> {

                    var view: View? = currentFocus
                    if (view == null)
                        view = View(applicationContext)

                    imm!!.hideSoftInputFromWindow(view.windowToken, 0)

                    thirdCircle!!.setTextColor(Color.WHITE)
                    firstCircle!!.setTextColor(Color.GRAY)
                    secondCircle!!.setTextColor(Color.GRAY)

                    buttonBack!!.isEnabled = true
                    buttonBack!!.text = getString(R.string.word_back)
                    buttonNext!!.text = getString(R.string.word_finish)

                    buttonNext!!.setOnClickListener {
                        finish()
                    }

                    buttonBack!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 1
                    }

                }
            }

        }
    }
}