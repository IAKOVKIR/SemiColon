package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
    private var username: String = ""
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n: SharedPreferences = context!!.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        userID = n.getInt("id", 1)
        username = n.getString("username", "")!!

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(context!!)

        //TextView representing user's full name
        val nameText: TextView = view.findViewById(R.id.name)
        nameText.text = username

        //TextView representing user's phone number
        val phoneNum: TextView = view.findViewById(R.id.phone_number)

        val numOfFollowers: TextView = view.findViewById(R.id.followers_number)
        val numOfFollowing: TextView = view.findViewById(R.id.following_number)

        //TextView representing user's email
        val email: TextView = view.findViewById(R.id.email)

        job = CoroutineScope(Dispatchers.Default).launch {

            //var fName = ""
            //var lName = ""
            var userPhone = ""
            var emailText = ""
            var followers = 0
            var following = 0

            withContext(Dispatchers.Default) {
                //fName = db.getUsersData(userID, "UserFirstName")
                //lName = db.getUsersData(userID, "UserLastName")
                val line: String = db.getUsersData(userID, "Phone")
                //variable phoneImp contains a string of phone number ("#(###)### ###")
                if (line.isNotEmpty()) {
                    userPhone = "${line[0]}(${line.substring(1, 4)})${line.substring(
                        4, 7
                    )} ${line.substring(7, 10)}"
                }


                emailText = db.getUsersData(userID, "Email")
                followers = db.countFollowers(userID)
                following = db.countFollowing(userID)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                //nameText.text = "$fName\n$lName"
                phoneNum.text = userPhone
                email.text = emailText
                numOfFollowers.text = "$followers"
                numOfFollowing.text = "$following"
            }

        }

        val followersLink: LinearLayout = view.findViewById(R.id.linear_layout_followers)
        val followingLink: LinearLayout = view.findViewById(R.id.linear_layout_following)

        followersLink.setOnClickListener {
            val intent = Intent(activity, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$userID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$userID")
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

        //Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}