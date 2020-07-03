package com.example.semicolon.semi_registration

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.R

class SliderAdapter(private val context : Context) : PagerAdapter() {

    private val slideTexts: Array<String> = arrayOf("first name", "email", "phone number")
    private var check = false

    override fun getCount(): Int {
        return slideTexts.size
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.registration_slide_layout, container, false)

        //layouts
        val firstLayout: ScrollView = view.findViewById(R.id.first_layout)
        val secondLayout: ScrollView = view.findViewById(R.id.second_layout)
        val thirdLayout: RelativeLayout = view.findViewById(R.id.third_layout)

        //val firstName: TextInputEditText = view.findViewById(R.id.first_name)

        /*firstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val line: String = firstName.text.toString()
                Log.e("test",line)

            }
        })*/

        val checkBox: CheckBox = view.findViewById(R.id.checkBox)

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