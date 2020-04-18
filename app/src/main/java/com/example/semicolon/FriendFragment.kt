package com.example.semicolon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.support_features.Time
//import de.hdodenhof.circleimageview.CircleImageView

class FriendFragment : Fragment() {

    val MY_ID = "my_id"
    val USER_ID = "user_id"
    val EXCEPTION_ID = "exception_id"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_friend, container, false)

        //DatabaseOpenHelper object
        val db = DatabaseOpenHelper(context!!)

        //Time object
        val time = Time()

        val myID: Int = arguments!!.getInt(MY_ID)
        val userID: Int = arguments!!.getInt(USER_ID)
        val exceptionID: Int = arguments!!.getInt(EXCEPTION_ID)
        val userObject: User = db.findUserByID(userID)

        //val circleImageView: CircleImageView = findViewById(R.id.circleImageView)

        //TextViews
        val name: TextView = view.findViewById(R.id.name)
        val phone: TextView = view.findViewById(R.id.phone_number)
        val email: TextView = view.findViewById(R.id.email)
        val numOfFollowers: TextView = view.findViewById(R.id.followers_number)
        val numOfFollowing: TextView = view.findViewById(R.id.following_number)

        //Buttons
        val followButton: Button = view.findViewById(R.id.followButton)
        val backButton: ImageView = view.findViewById(R.id.back_button)

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_friend", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        //Layouts
        val followersLayout: LinearLayout = view.findViewById(R.id.linear_layout_followers)
        val followingLayout: LinearLayout = view.findViewById(R.id.linear_layout_following)
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

        val fullName: String = userObject.fullName
        val phoneNum = "${userObject.phone[0]}(${userObject.phone.substring(1, 4)})${userObject.phone.substring(4, 7)} ${userObject.phone.substring(7, 10)}"

        name.text = fullName
        phone.text = phoneNum
        email.text = userObject.email
        numOfFollowers.text = "${db.countFollowers(userID)}"
        numOfFollowing.text = "${db.countFollowing(userID)}"
        followButton.text = str[bool + 1]

        followButton.setOnClickListener{
            if (bool == 1) {
                followButton.text = str[1]
                bool = 0
                val friend = Friend(
                    db.countFriendTable(), myID, userID,
                    time.getDate(), time.getTime(), 0
                )
                db.insertRequest(friend)
            } else {
                followButton.text = str[2]
                bool = 1
                db.deleteFollowing(myID, userID)
            }
        }

        /*followersLayout.setOnClickListener {
            val intent = Intent(this, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$myID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$exceptionID")
            intent.putExtra("string", 0)
            startActivity(intent)
        }

        followingLayout.setOnClickListener {
            val intent = Intent(this, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$myID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$exceptionID")
            intent.putExtra("string", 1)
            startActivity(intent)
        }*/
        return view
    }
}