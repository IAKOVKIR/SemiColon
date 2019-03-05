package com.example.semicolon

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class SliderAdapter(private val context : Context) : PagerAdapter() {

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

        val text = view.findViewById<TextView>(R.id.textFirstName)
        val editText = view.findViewById<EditText>(R.id.editTextFirstName)

        val text2 = view.findViewById<TextView>(R.id.textSecondName)
        val editText2 = view.findViewById<EditText>(R.id.editTextSecondName)

        val agreement = view.findViewById<RelativeLayout>(R.id.TermsAndConditions)
        val mainLayout = view.findViewById<RelativeLayout>(R.id.MainLayout)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                texts[position] = editText.text.toString()
            }
        })

        editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                texts2[position] = editText2.text.toString()
            }
        })

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            check = true
        }

        if (position == 2) {
            agreement.visibility = View.VISIBLE
            mainLayout.visibility = View.GONE
            editText.visibility = View.GONE
            text.visibility = View.GONE
            editText2.visibility = View.GONE
            text2.visibility = View.GONE
        }

        text.text = slideTexts[position]
        text2.text = slideTexts2[position]
        checkBox.isChecked = check

        editText.setText(texts[position])
        editText2.setText(texts2[position])

        editText.hint = slideEditTexts[position]
        editText2.hint = slideEditTexts2[position]

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}