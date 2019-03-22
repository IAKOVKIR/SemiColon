package com.example.semicolon

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "user"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private var param1 : ArrayList<String>? = null
    private var settingsButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getStringArrayList(ARG_PARAM1)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val fullName = "${param1?.get(1)} ${param1?.get(2)}"
        val phoneImp = 0//"${param1?.get(3)?.get(0)}(${param1?.get(3)!!.substring(1, 4)})${param1?.get(3)!!.substring(4, 7)} ${param1?.get(3)!!.substring(7, 10)}"
        val emailText = "${param1?.get(7)}".trim()

        val nameText = view.findViewById<TextView>(R.id.name)
        val phoneNum = view.findViewById<TextView>(R.id.phone_number)
        val location = view.findViewById<TextView>(R.id.location)
        val email = view.findViewById<TextView>(R.id.email)

        val friendsLink = arrayOf<TextView>(view.findViewById(R.id.friends_word), view.findViewById(R.id.friends_number))

        nameText.text = fullName
        phoneNum.text = "Phone: $phoneImp"
        location.text = "Location: ${param1?.get(5)}, Australia"

        if (emailText == "")
            email.visibility = View.GONE
        else
            email.text = "Email: $emailText"

        val args = Bundle()
        args.putStringArrayList("user", param1)

        friendsLink[0].setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        friendsLink[1].setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        settingsButton = view.findViewById(R.id.settings_button)
        settingsButton!!.setOnClickListener {
            val fragment: Fragment = SettingFragment()
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            //transaction.addToBackStack(null)
            // Commit the transaction
            transaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }

}