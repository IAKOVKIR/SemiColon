package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.semicolon.following_followers.FollowingFollowersActivity
import com.example.semicolon.semi_settings.SettingFragment
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {


    private var userID: Int = 0
    private var userPhone: String = ""
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n: SharedPreferences = context!!.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        userID = n.getInt("id", 1)
        userPhone = n.getString("phone", "")!!

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(context!!)

        //variable phoneImp contains a string of phone number ("#(###)### ###")
        var phoneImp = ""
        if (userPhone.isNotEmpty()) {
            phoneImp = "${userPhone[0]}(${userPhone.substring(1, 4)})${userPhone.substring(
                4,
                7
            )} ${userPhone.substring(7, 10)}"
        }

        //variable emailText contains email user's address
        var emailText = ""//email

        //TextView representing user's full name
        val nameText: TextView = view.findViewById(R.id.name)
        nameText.text = ""

        //TextView representing user's phone number
        val phoneNum: TextView = view.findViewById(R.id.phone_number)
        phoneNum.text = phoneImp

        val numOfFollowers: TextView = view.findViewById(R.id.followers_number)
        val numOfFollowing: TextView = view.findViewById(R.id.following_number)
        numOfFollowers.text = "${db.countFollowers(userID)}"
        numOfFollowing.text = "${db.countFollowing(userID)}"

        //TextView representing user's location
        val location: TextView = view.findViewById(R.id.location)
        location.text = ""//city

        //TextView representing user's email
        val email: TextView = view.findViewById(R.id.email)
        email.text = emailText

        job = CoroutineScope(Dispatchers.Default).launch {

            var fName = ""
            var lName = ""
            var userCity = ""

            withContext(Dispatchers.Default) {
                fName = db.getUsersData(userID, "UserFirstName")
                lName = db.getUsersData(userID, "UserLastName")
                emailText = db.getUsersData(userID, "Email")
                userCity = db.getUsersData(userID, "City")
            }

            nameText.text = "$fName\n$lName"
            email.text = emailText
            location.text = userCity

        }

        val followersLink: LinearLayout = view.findViewById(R.id.linear_layout_followers)
        val followingLink: LinearLayout = view.findViewById(R.id.linear_layout_following)

        followersLink.setOnClickListener {
            val intent = Intent(activity, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$userID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$userID")
            Log.i("check", "$userID")
            intent.putExtra("string", 0)
            startActivity(intent)
        }

        followingLink.setOnClickListener {
            val intent = Intent(activity, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$userID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$userID")
            intent.putExtra("string", 1)
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

        //db.close()
        //Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}