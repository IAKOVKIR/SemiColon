package com.example.semicolon

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.time.Time

class FriendActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        //DatabaseOpenHelper object
        val db = DatabaseOpenHelper(applicationContext)

        //Time object
        val time = Time()

        val myID: Int = intent.getIntExtra("MyID", -1)
        val userID: Int = intent.getIntExtra("UserID", -1)
        val userObject: User = db.findUserByID(userID)

        //TextView's
        val name: TextView = findViewById(R.id.name)
        val location: TextView = findViewById(R.id.location)
        val phone: TextView = findViewById(R.id.phone_number)
        val email: TextView = findViewById(R.id.email)
        val numOfFollowers: TextView = findViewById(R.id.followers_number)
        val numOfFollowing: TextView = findViewById(R.id.following_number)

        //Button's
        val followButton: Button = findViewById(R.id.followButton)
        val backButton: ImageButton = findViewById(R.id.back_button)

        /*
        Variable bool contains one of three conditions of "friendship":
        "-1" - you follow the user
        "0" - "your request is in progress"
        "1" - "you do not follow the user"
        */
        var bool: Int = db.checkFollower(userID, myID)

        //Array str contains 3 of these conditions
        val str: Array<String> = arrayOf("unfollow", "in progress", "follow")

        val fullName = "${userObject.firstName}\n${userObject.lastName}"
        val phoneNum = "${userObject.phone[0]}(${userObject.phone.substring(1, 4)})${userObject.phone.substring(4, 7)} ${userObject.phone.substring(7, 10)}"

        name.text = fullName
        location.text = userObject.city
        phone.text = phoneNum
        email.text = userObject.email
        numOfFollowers.text = "${db.countFollowers(userID)}"
        numOfFollowing.text = "${db.countFollowing(userID)}"
        followButton.text = str[bool + 1]

        followButton.setOnClickListener{
            if (bool == 1) {
                followButton.text = str[1]
                bool = 0
                val friend = Friend(db.countFriendTable(), myID, userID,
                    time.getDate(), time.getTime(), 0)
                db.insertRequest(friend)
            } else {
                followButton.text = str[2]
                bool = 1
                db.deleteFollowing(myID, userID)
            }
        }

        backButton.setOnClickListener { finish() }
    }
}