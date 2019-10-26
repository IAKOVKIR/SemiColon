package com.example.semicolon

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.widget.*
import com.example.semicolon.following_followers.FollowingFollowersActivity
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.time.Time
import de.hdodenhof.circleimageview.CircleImageView

class FriendActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        //DatabaseOpenHelper object
        val db = DatabaseOpenHelper(applicationContext)

        //Time object
        val time = Time()

        val myID: Int = intent.getIntExtra("my_id", -1)
        val userID: Int = intent.getIntExtra("user_id", -1)
        val exceptionID: Int = intent.getIntExtra("exception_id", -1)
        val userObject: User = db.findUserByID(userID)

        val circleImageView: CircleImageView = findViewById(R.id.circleImageView)

        //TextViews
        val name: TextView = findViewById(R.id.name)
        val location: TextView = findViewById(R.id.location)
        val phone: TextView = findViewById(R.id.phone_number)
        val email: TextView = findViewById(R.id.email)
        val numOfFollowers: TextView = findViewById(R.id.followers_number)
        val numOfFollowing: TextView = findViewById(R.id.following_number)

        //Buttons
        val followButton: Button = findViewById(R.id.followButton)
        val backButton: ImageView = findViewById(R.id.back_button)

        //Layouts
        val followersLayout: LinearLayout = findViewById(R.id.linear_layout_followers)
        val followingLayout: LinearLayout = findViewById(R.id.linear_layout_following)
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

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

        followersLayout.setOnClickListener {
            val intent = Intent(this, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$myID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$exceptionID")
            intent.putExtra("string", "0")
            startActivity(intent)
        }

        followingLayout.setOnClickListener {
            val intent = Intent(this, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$myID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$exceptionID")
            intent.putExtra("string", "1")
            startActivity(intent)
        }
    }
}