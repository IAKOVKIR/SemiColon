package com.example.semicolon

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class SliderAdapter(private val context : Context) : PagerAdapter() {

    //arrays with strings for pages
    private val slideTexts = arrayOf("first name", "email", "phone number")
    private val slideTexts2 = arrayOf("second name", "city", "age")
    private val slideEditTexts = arrayOf("Enter your first name", "Enter your email", "Enter your phone number")
    private val slideEditTexts2 = arrayOf("enter your second name", "enter your city", "enter your age")
    val texts = arrayOf("", "", "")
    val texts2 = arrayOf("", "", "")
    private var check = false

    override fun getCount(): Int {
        return slideTexts.size
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.slide_layout, container, false)

        //layouts
        val firstLayout = view.findViewById<ScrollView>(R.id.first_layout)
        val secondLayout = view.findViewById<ScrollView>(R.id.second_layout)
        val thirdLayout = view.findViewById<RelativeLayout>(R.id.third_layout)

        //EditTexts
        val firstName = view.findViewById<TextInputEditText>(R.id.first_name)
        val lastName = view.findViewById<TextInputEditText>(R.id.last_name)
        //val phone = view.findViewById<TextInputEditText>(R.id.phone)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)


        firstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        lastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        checkBox.setOnCheckedChangeListener { _, _ ->
            check = true
        }

        firstLayout.visibility = View.GONE
        secondLayout.visibility = View.GONE
        thirdLayout.visibility = View.GONE

        when (position) {
            0 -> firstLayout.visibility = View.VISIBLE
            1 -> secondLayout.visibility = View.VISIBLE
            2 -> thirdLayout.visibility = View.VISIBLE
        }

        checkBox.isChecked = check

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}