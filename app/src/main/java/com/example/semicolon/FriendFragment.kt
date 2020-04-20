package com.example.semicolon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.semicolon.following_followers.FollowingFollowersFragment
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.support_features.Time
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameters, e.g MY_ID, USER_ID and EXCEPTION_ID
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"
private const val EXCEPTION_ID = "exception_id"
private const val SLIDE_NUMBER = "slide_number"

class FriendFragment : Fragment() {

    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private var db: DatabaseOpenHelper? = null
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapDrawable: BitmapDrawable
    //Array str contains 3 of these conditions
    private val str: Array<String> = arrayOf("unfollow", "in progress", "follow")
    //Time object
    val time = Time()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID)
            userID = it.getInt(USER_ID)
            exceptionID = it.getInt(EXCEPTION_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_friend, container, false)
        //DatabaseOpenHelper object
        db = DatabaseOpenHelper(context!!)

        //TextViews
        val name: TextView = view.findViewById(R.id.name)
        val phone: TextView = view.findViewById(R.id.phone_number)
        val email: TextView = view.findViewById(R.id.email)
        val numOfFollowers: TextView = view.findViewById(R.id.followers_number)
        val numOfFollowing: TextView = view.findViewById(R.id.following_number)

        val circleImageView: CircleImageView = view.findViewById(R.id.circleImageView)

        //Buttons
        val followButton: TextView = view.findViewById(R.id.follow_un_follow_button)
        val backButton: ImageView = view.findViewById(R.id.back_button)

        //Layouts
        val followersLayout: LinearLayout = view.findViewById(R.id.linear_layout_followers)
        //val followingLayout: LinearLayout = view.findViewById(R.id.linear_layout_following)
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

        var bool = 0

        CoroutineScope(Dispatchers.Default).launch {
            var userObject = User()
            var phoneNum = ""
            var followers = 0
            var following = 0

            withContext(Dispatchers.Default) {
                userObject = db!!.findUserByID(userID!!)
                //variable phoneImp contains a string of phone number ("#(###)### ###")
                if (userObject.phone.isNotEmpty())
                    phoneNum = "${userObject.phone[0]}(${userObject.phone.substring(1, 4)})${userObject.phone.substring(4, 7)} ${userObject.phone.substring(7, 10)}"

                followers = db!!.countFollowers(userID!!)
                following = db!!.countFollowing(userID!!)
                bool = db!!.checkFollower(myID!!, userID!!)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                name.text = userObject.username
                phone.text = phoneNum
                email.text = userObject.email
                numOfFollowers.text = "$followers"
                numOfFollowing.text = "$following"
                followButton.text = str[bool + 1]
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            withContext(Dispatchers.Default) {
                bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.burns)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val dif: Double = height.toDouble() / width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(view.context!!.resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                circleImageView.setImageDrawable(bitmapDrawable)
            }

        }

        //OnClickListener's

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_friend", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        /*
        Variable bool contains one of three conditions of "friendship":
        "1" - you follow the user
        "0" - "your request is in progress"
        "-1" - "you do not follow the user"
        */

        followButton.setOnClickListener{

            if (bool == -1)
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        val friend = Friend(myID!!, userID!!,
                            time.getDate(), time.getTime(), 0)
                        res = db!!.insertRequest(friend)
                    }

                    launch (Dispatchers.Main) {
                        if (res) {
                            followButton.text = str[1]
                            bool = 0
                        }
                    }
                }
            else
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        res = db!!.deleteFollowing(myID!!, userID!!)
                    }

                    launch (Dispatchers.Main) {
                        if (res) {
                            followButton.text = str[2]
                            bool = 1
                        }
                    }
                }

        }

        followersLayout.setOnClickListener {
            sendToFollowersFollowing(0)
        }

        return view
    }

    private fun sendToFollowersFollowing(slideNumber: Int) {
        val fragment = FollowingFollowersFragment()
        val argument = Bundle()
        argument.putInt(MY_ID, userID!!)
        argument.putInt(USER_ID, userID!!)
        argument.putInt(EXCEPTION_ID, userID!!)
        argument.putInt(SLIDE_NUMBER, slideNumber)
        fragment.arguments = argument
        parentFragmentManager
            .beginTransaction()
            .addToBackStack("to_followers_following")
            .replace(R.id.nav_host, fragment, "to_followers_following")
            .commit()
    }

}


/*      followingLayout.setOnClickListener {
            val intent = Intent(this, FollowingFollowersActivity::class.java)
            intent.putExtra("my_id", "$myID")
            intent.putExtra("user_id", "$userID")
            intent.putExtra("exception_id", "$exceptionID")
            intent.putExtra("string", 1)
            startActivity(intent)
        }*/