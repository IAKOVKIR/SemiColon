package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.followers_requests.FollowersActivity
import com.example.semicolon.following_search.FollowingActivity
import com.example.semicolon.semi_settings.SettingFragment

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    //variable param1 contains ArrayList<String> array with user's data from shared preferences
    private lateinit var myData: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val log = Login()
        val n: SharedPreferences = context!!.getSharedPreferences(log.prefName, Context.MODE_PRIVATE)

        myData = arrayOf(n.getInt(log.prefVar[0], 1).toString(), n.getString(log.prefVar[1], "")!!,
            n.getString(log.prefVar[2], "")!!, n.getString(log.prefVar[3], "")!!,
            n.getString(log.prefVar[5], "")!!, n.getString(log.prefVar[7], "")!!)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(context!!)

        //variable fullName contains a string with user's first and last names ("firstName lastName")
        val fullName = "${myData[1]}\n${myData[2]}"

        //variable phoneImp contains a string of phone number ("#(###)### ###")
        val phoneImp = "${myData[3][0]}(${myData[3].substring(1, 4)})${myData[3].substring(4, 7)} ${myData[3].substring(7, 10)}"

        //variable emailText contains email user's address
        val emailText: String = myData[5].trim()

        //TextView representing user's full name
        val nameText: TextView = view.findViewById(R.id.name)
        nameText.text = fullName

        //TextView representing user's phone number
        val phoneNum: TextView = view.findViewById(R.id.phone_number)
        phoneNum.text = phoneImp

        val numOfFollowers: TextView = view.findViewById(R.id.followers_number)
        val numOfFollowing: TextView = view.findViewById(R.id.following_number)
        numOfFollowers.text = "${db.countFollowers(myData[0].toInt())}"
        numOfFollowing.text = "${db.countFollowing(myData[0].toInt())}"

        //TextView representing user's location
        val location: TextView = view.findViewById(R.id.location)
        location.text = myData[4]

        //TextView representing user's email
        val email: TextView = view.findViewById(R.id.email)
        email.text = emailText

        val followersLink: LinearLayout = view.findViewById(R.id.linear_layout_followers)
        val followingLink: LinearLayout = view.findViewById(R.id.linear_layout_following)

        followersLink.setOnClickListener {
            val intent = Intent(activity, FollowersActivity::class.java)
            intent.putExtra("MyID", myData[0])
            startActivity(intent)
        }

        followingLink.setOnClickListener {
            val intent = Intent(activity, FollowingActivity::class.java)
            intent.putExtra("MyID", myData[0])
            startActivity(intent)
        }

        val settingsButton : ImageButton = view.findViewById(R.id.settings_button)
        settingsButton.setOnClickListener {
            val fragment: Fragment = SettingFragment()
            val manager: FragmentManager? = fragmentManager
            val transaction: FragmentTransaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            transaction.commit()
        }

        db.close()
        // Inflate the layout for this fragment
        return view
    }
}