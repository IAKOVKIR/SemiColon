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
    private val slideEditTexts = arrayOf("Enter your first name", "Enter your email", "Enter your phone number")
    val texts = arrayOf("", "", "")

    override fun getCount(): Int {
        return slideTexts.size
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.slide_layout, container, false)

        val text = view.findViewById<TextView>(R.id.textView2)
        val editText = view.findViewById<EditText>(R.id.editText2)
        val agreement = view.findViewById<RelativeLayout>(R.id.TermsAndConditions)
        val mainLayout = view.findViewById<RelativeLayout>(R.id.MainLayout)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                texts[position] = editText.text.toString()
            }
        })

        if (position == 2) {
            agreement.visibility = View.VISIBLE
            mainLayout.visibility = View.GONE
            editText.visibility = View.GONE
            text.visibility = View.GONE
        }

        text.text = slideTexts[position]
        editText.setText(texts[position])

        editText.hint = slideEditTexts[position]

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}