package com.example.semicolon

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.semicolon.users.UserFollowersFragment

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 *
 */
class FriendFragment : Fragment() {

    private var param4: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param4 = it.getStringArrayList(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        val linearFollowers = view.findViewById<LinearLayout>(R.id.linear_layout_followers)

        val name = view.findViewById<TextView>(R.id.name)
        val location = view.findViewById<TextView>(R.id.location)
        val phone = view.findViewById<TextView>(R.id.phone_number)
        val email = view.findViewById<TextView>(R.id.email)

        linearFollowers.setOnClickListener {
            val fragment = UserFollowersFragment()
            val args = Bundle()
            args.putStringArrayList("user", param4)
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        val backButton = view.findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        val fullName = "${param4!![1]} ${param4!![2]}"
        val phoneNum = "${param4!![3][0]}(${param4!![3].substring(1, 4)})${param4!![3].substring(4, 7)} ${param4!![3].substring(7, 10)}"

        name.text = fullName
        location.text = param4!![4]
        phone.text = phoneNum
        email.text = param4!![6]

        return view
    }

    override fun onResume() {
        super.onResume()
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                fragmentManager!!.popBackStack()
                /*val args = Bundle()
                args.putStringArrayList("user", param4)

                val fragment: Fragment = MainFragment()
                fragment.arguments = args
                val manager = fragmentManager
                val transaction = manager!!.beginTransaction()
                transaction.replace(R.id.nav_host, fragment)
                // Commit the transaction
                transaction.commit()*/

                true
            } else
                false
        }
    }

}