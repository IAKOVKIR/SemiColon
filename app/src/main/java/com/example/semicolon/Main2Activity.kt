package com.example.semicolon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.Button
import android.widget.TextView

class Main2Activity : Activity() {

    private var mSlideViewPager : ViewPager? = null
    private var sliderAdapter : SliderAdapter? = null

    var firstCircle : TextView? = null
    var secondCircle : TextView? = null
    var thirdCircle : TextView? = null

    var buttonNext : Button? = null
    var buttonBack : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mSlideViewPager = findViewById(R.id.slideViewPager)

        buttonBack = findViewById(R.id.buttonBack)
        buttonNext = findViewById(R.id.buttonNext)

        buttonNext!!.setOnClickListener {
            mSlideViewPager!!.currentItem = 1
        }

        firstCircle = findViewById(R.id.FirstCircle)
        secondCircle = findViewById(R.id.SecondCircle)
        thirdCircle = findViewById(R.id.ThirdCircle)

        sliderAdapter = SliderAdapter(this)

        mSlideViewPager?.adapter = sliderAdapter
        mSlideViewPager!!.addOnPageChangeListener(pageChangeListener)

    }

    var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            val drawCircle = getDrawable(R.drawable.regcircle)
            val drawUpdatedCircle = getDrawable(R.drawable.regupdatecircle)

            when (position) {
                0 -> {
                    firstCircle!!.background = drawUpdatedCircle
                    secondCircle!!.background = drawCircle
                    thirdCircle!!.background = drawCircle

                    buttonBack!!.isEnabled = false
                    buttonNext!!.isEnabled = true
                    buttonBack!!.text = ""
                    buttonNext!!.text = "next"

                    buttonNext!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 1
                    }

                }
                1 -> {
                    secondCircle!!.background = drawUpdatedCircle
                    firstCircle!!.background = drawCircle
                    thirdCircle!!.background = drawCircle

                    buttonBack!!.isEnabled = true
                    buttonNext!!.isEnabled = true
                    buttonBack!!.text = "back"
                    buttonNext!!.text = "next"

                    buttonNext!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 2
                    }

                    buttonBack!!.setOnClickListener {
                        mSlideViewPager!!.currentItem = 0
                    }

                }
                else -> {
                    thirdCircle!!.background = drawUpdatedCircle
                    firstCircle!!.background = drawCircle
                    secondCircle!!.background = drawCircle

                    buttonBack!!.isEnabled = true
                    buttonNext!!.isEnabled = true
                    buttonBack!!.text = "back"
                    buttonNext!!.text = "finish"

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
