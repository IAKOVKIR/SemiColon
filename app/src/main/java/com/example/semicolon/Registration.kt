package com.example.semicolon

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.view.View

class Registration : Activity() {

    private var mSlideViewPager : ViewPager? = null
    private var sliderAdapter : SliderAdapter? = null

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
        setContentView(R.layout.activity_registration)

        mSlideViewPager = findViewById(R.id.slideViewPager)

        buttonBack = findViewById(R.id.buttonBack)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext!!.setOnClickListener {
            mSlideViewPager!!.currentItem = 1
        }

        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        firstCircle = findViewById(R.id.FirstCircle)
        secondCircle = findViewById(R.id.SecondCircle)
        thirdCircle = findViewById(R.id.ThirdCircle)

        sliderAdapter = SliderAdapter(this)

        mSlideViewPager?.adapter = sliderAdapter
        mSlideViewPager!!.addOnPageChangeListener(pageChangeListener)

    }

    private var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            val drawCircle = getDrawable(R.drawable.reg_circle)
            val drawUpdatedCircle = getDrawable(R.drawable.reg_update_circle)

            when (position) {
                0 -> {
                    firstCircle!!.background = drawUpdatedCircle
                    firstCircle!!.setTextColor(Color.BLACK)
                    secondCircle!!.background = drawCircle
                    thirdCircle!!.background = drawCircle
                    secondCircle!!.setTextColor(Color.WHITE)
                    thirdCircle!!.setTextColor(Color.WHITE)

                    buttonBack!!.isEnabled = false
                    buttonBack!!.text = ""
                    buttonNext!!.text = getString(R.string.word_next)

                    buttonNext!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 1
                    }

                }
                1 -> {
                    secondCircle!!.background = drawUpdatedCircle
                    secondCircle!!.setTextColor(Color.BLACK)
                    firstCircle!!.background = drawCircle
                    thirdCircle!!.background = drawCircle
                    firstCircle!!.setTextColor(Color.WHITE)
                    thirdCircle!!.setTextColor(Color.WHITE)

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

                    var view = currentFocus
                    if (view == null) {
                        view = View(applicationContext)
                    }
                    imm!!.hideSoftInputFromWindow(view.windowToken, 0)

                    thirdCircle!!.background = drawUpdatedCircle
                    thirdCircle!!.setTextColor(Color.BLACK)
                    firstCircle!!.background = drawCircle
                    secondCircle!!.background = drawCircle
                    firstCircle!!.setTextColor(Color.WHITE)
                    secondCircle!!.setTextColor(Color.WHITE)

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