package com.example.semicolon

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView


class SliderAdapter(private val context : Context) : PagerAdapter() {

    val slideTexts = arrayOf("first name", "email", "phone number")
    val slideEditTexts = arrayOf("Enter your first name", "Enter your email", "Enter your phone number")
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

        val navBar = view.findViewById<TextView>(R.id.FirstCircle)
        val text = view.findViewById<TextView>(R.id.textView2)
        val editText = view.findViewById<EditText>(R.id.editText2)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                texts[position] = editText.text.toString()
            }
        })

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
