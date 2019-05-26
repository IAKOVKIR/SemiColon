package com.example.semicolon

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.semicolon.semi_database.DatabaseOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class FriendActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val intent = intent
        val param3 = intent.getStringExtra("param1")
        val param4 = intent.getStringArrayListExtra("param2")


        val name = findViewById<TextView>(R.id.name)
        val location = findViewById<TextView>(R.id.location)
        val phone = findViewById<TextView>(R.id.phone_number)
        val email = findViewById<TextView>(R.id.email)

        val numOfFollowers = findViewById<TextView>(R.id.followers_number)
        val numOfFollowing = findViewById<TextView>(R.id.following_number)

        val followButton = findViewById<Button>(R.id.followButton)
        val db = DatabaseOpenHelper(applicationContext)

        var bool = db.checkFollower(param3, param4!![0])
        val str = arrayOf("in progress", "follow", "unfollow")

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        when (bool) {
            1 -> followButton.text = str[2]
            2 -> followButton.text = str[1]
            else -> followButton.text = str[0]
        }

        followButton.setOnClickListener{
            if (bool == 2) {
                followButton.text = str[0]
                bool = 3
                val friend = Friend(db.countFriendTable(), param4[0].toInt(), param3.toInt(),
                    strDate.substring(11, 19), strDate.substring(0, 10), bool)
                db.insertRequest(friend)
            } else {
                followButton.text = str[1]
                bool = 2
                db.deleteFollowing(param4[0], param3)
            }
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener { finish() }

        val fullName = "${param4[1]}\n${param4[2]}"
        val phoneNum = "${param4[3][0]}(${param4[3].substring(1, 4)})${param4[3].substring(4, 7)} ${param4[3].substring(7, 10)}"

        name.text = fullName
        location.text = param4[4]
        phone.text = phoneNum
        email.text = param4[6]
        numOfFollowers.text = "${db.countFollowers(param4[0])}"
        numOfFollowing.text = "${db.countFollowing(param4[0])}"

    }
}
