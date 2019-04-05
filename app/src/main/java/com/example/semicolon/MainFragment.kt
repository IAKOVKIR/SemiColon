package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 */

class MainFragment : Fragment() {

    /*
    variable param1 contains ArrayList<String> array with user's data from shared preferences
    log is an object from Login Class
     */
    private var param1 : ArrayList<String>? = null
    private var settingsButton : ImageButton? = null
    private var data: DatabaseOpenHelper? = null
    private var log = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val n = context!!.getSharedPreferences(log.prefName, Context.MODE_PRIVATE)

        //assigning values to ArrayList<String>
        param1 = arrayListOf(n.getString(log.prefVar[0], "") as String, n.getString(log.prefVar[1], "") as String,
            n.getString(log.prefVar[2], "") as String, n.getString(log.prefVar[3], "") as String,
            n.getString(log.prefVar[4], "") as String, n.getString(log.prefVar[5], "") as String,
            n.getString(log.prefVar[6], "") as String, n.getString(log.prefVar[7], "") as String)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        data = context?.let { DatabaseOpenHelper(it) }

        //variable fullName contains a string with user's first and last names ("firstName lastName")
        val fullName = "${param1!![1]} ${param1!![2]}"

        //variable phoneImp contains a string of phone number ("#(###)### ###")
        val phoneImp = "${param1!![3][0]}(${param1!![3].substring(1, 4)})${param1!![3].substring(4, 7)} ${param1!![3].substring(7, 10)}"

        //variable emailText contains email user's address
        val emailText = param1!![7].trim()

        //TextView representing user's full name
        val nameText = view.findViewById<TextView>(R.id.name)
        nameText.text = fullName

        //TextView representing user's phone number
        val phoneNum = view.findViewById<TextView>(R.id.phone_number)
        phoneNum.text = phoneImp

        val numOfFollowers = view.findViewById<TextView>(R.id.followers_number)
        val numOfFollowing = view.findViewById<TextView>(R.id.following_number)
        numOfFollowers.text = "${data!!.countFollowers(param1!![0])}"
        numOfFollowing.text = "${data!!.countFollowing(param1!![0])}"

        //TextView representing user's location
        val location = view.findViewById<TextView>(R.id.location)
        location.text = param1!![5]

        //TextView representing user's email
        val email = view.findViewById<TextView>(R.id.email)
        if (emailText == "")
            email.visibility = View.GONE
        else
            email.text = emailText

        val followersLink = view.findViewById<LinearLayout>(R.id.linear_layout_followers)
        val followingLink = view.findViewById<LinearLayout>(R.id.linear_layout_following)

        //create args list for Friends and Settings fragments
        val args = Bundle()
        args.putStringArrayList("user", param1)

        followersLink.setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            args.putInt("block", 1)
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        followingLink.setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            args.putInt("block", 2)
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        settingsButton = view.findViewById(R.id.settings_button)
        settingsButton!!.setOnClickListener {
            val fragment: Fragment = SettingFragment()
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            //transaction.addToBackStack(null)
            // Commit the transaction
            transaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }

}