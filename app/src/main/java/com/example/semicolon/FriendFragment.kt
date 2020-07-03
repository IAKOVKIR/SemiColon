package com.example.semicolon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.semicolon.databinding.FragmentFriendBinding
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameters, e.g MY_ID, USER_ID and EXCEPTION_ID
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"

class FriendFragment : Fragment() {

    private var myID: Int? = null
    private var userID: Int? = null
    lateinit var db: DatabaseOpenHelper
    //Array str contains 3 of these conditions
    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")//-1, 0, 1
    //Time object
    val time = Time()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID) //myID
            userID = it.getInt(USER_ID) //userID
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentFriendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_friend, container, false)
        //DatabaseOpenHelper object
        db = DatabaseOpenHelper(requireContext())

        //TextViews
        val followedBy: TextView = binding.followedBy
        val followUnFollowButton: TextView = binding.followUnFollowButton

        //Layouts
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

        var bool = 0

        CoroutineScope(Dispatchers.Default).launch {
            var userObject = User()
            var phoneNum = ""
            var followedByList: ArrayList<String>
            var followedByLine = ""
            var followers = 0
            var following = 0

            withContext(Dispatchers.Default) {
                userObject = db.findUserByID(userID!!)
                //variable phoneImp contains a string of phone number ("#(###)### ###")
                if (userObject.phone.isNotEmpty())
                    phoneNum = "${userObject.phone[0]}(${userObject.phone.substring(1, 4)})${userObject.phone.substring(4, 7)} ${userObject.phone.substring(7, 10)}"

                followers = db.countFollowers(userID!!)
                following = db.countFollowing(userID!!)
                bool = db.checkFollower(myID!!, userID!!)
                followedByList = db.readFirstThreeMutualFollowers(myID!!, userID!!)

                val len: Int = followedByList.size
                followedByLine = when {
                    len == 3 -> "Followed by <b>${followedByList[0]}</b>, <b>${followedByList[1]}</b> and <b>1 other</b>"
                    len == 2 -> "Followed by <b>${followedByList[0]}</b> and <b>${followedByList[1]}</b>"
                    len == 1 -> "Followed by <b>${followedByList[0]}</b>"
                    len > 3 -> "Followed by <b>${followedByList[0]}</b>, <b>${followedByList[1]}</b> and <b>${len - 2} others</b>"
                    else -> ""
                }
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                binding.name.text = userObject.username
                binding.phoneNumber.text = phoneNum
                binding.email.text = userObject.email
                binding.followersNumber.text = "$followers"
                binding.followingNumber.text = "$following"
                followUnFollowButton.text = str[bool + 1]
                followedBy.isEnabled = true
                followedBy.text = HtmlCompat.fromHtml(followedByLine, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.burns)
                val dif: Double = bitmap.height.toDouble() / bitmap.width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(binding.root.context!!.resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                binding.circleImageView.setImageDrawable(bitmapDrawable)
            }

        }

        /*
        Variable bool contains one of three conditions of "friendship":
        "1" - you follow the user
        "0" - "your request is in progress"
        "-1" - "you do not follow the user"
        */

        followUnFollowButton.setOnClickListener{

            if (bool == -1) {
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        val friend = Follower(
                            myID!!, userID!!, 0,
                            time.toString(), time.toString()
                        )
                        res = db.insertRequest(friend)
                    }

                    launch(Dispatchers.Main) {
                        if (res) {
                            followUnFollowButton.text = str[1]
                            bool = 0
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        res = db.deleteFollowing(myID!!, userID!!)
                    }

                    launch(Dispatchers.Main) {
                        if (res) {
                            followUnFollowButton.text = str[2]
                            bool = 1
                        }
                    }
                }
            }
        }

        //OnClickListener's
        followedBy.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 0))
        }

        binding.linearLayoutFollowers.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 1))
        }

        binding.linearLayoutFollowing.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 2))
        }

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }
}