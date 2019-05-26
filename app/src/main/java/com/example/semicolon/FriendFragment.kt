package com.example.semicolon

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.semicolon.semi_database.DatabaseOpenHelper
import com.example.semicolon.users.UserFollowersFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 *
 */
class FriendFragment : Fragment() {

    private var param3: ArrayList<String>? = null
    private var param4: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param3 = it.getStringArrayList(ARG_PARAM3)
            param4 = it.getStringArrayList(ARG_PARAM4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        val linearFollowers = view.findViewById<LinearLayout>(R.id.linear_layout_followers)

        val name = view.findViewById<TextView>(R.id.name)
        val location = view.findViewById<TextView>(R.id.location)
        val phone = view.findViewById<TextView>(R.id.phone_number)
        val email = view.findViewById<TextView>(R.id.email)

        val numOfFollowers = view.findViewById<TextView>(R.id.followers_number)
        val numOfFollowing = view.findViewById<TextView>(R.id.following_number)

        val followButton = view.findViewById<Button>(R.id.followButton)
        val db = context?.let { DatabaseOpenHelper(it) }

        var bool = db!!.checkFollower(param3!![0], param4!![0])
        val str = arrayOf("in progress", "follow", "unfollow")

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        linearFollowers.setOnClickListener {
            val fragment = UserFollowersFragment()
            val args = Bundle()
            args.putStringArrayList("user", param4)
            args.putInt("id", param3!![0].toInt())
            fragment.arguments = args
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.add(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        when (bool) {
            1 -> followButton.text = str[2]
            2 -> followButton.text = str[1]
            else -> followButton.text = str[0]
        }

        followButton.setOnClickListener{
            if (bool == 2) {
                followButton.text = str[0]
                bool = 3
                val friend = Friend(db.countFriendTable(), param4!![0].toInt(), param3!![0].toInt(),
                    strDate.substring(11, 19), strDate.substring(0, 10), bool)
                db.insertRequest(friend)
            } else {
                followButton.text = str[1]
                bool = 2
                db.deleteFollowing(param4!![0], param3!![0])
            }
        }

        val backButton = view.findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        val fullName = "${param4!![1]}\n${param4!![2]}"
        val phoneNum = "${param4!![3][0]}(${param4!![3].substring(1, 4)})${param4!![3].substring(4, 7)} ${param4!![3].substring(7, 10)}"

        name.text = fullName
        location.text = param4!![4]
        phone.text = phoneNum
        email.text = param4!![6]
        numOfFollowers.text = "${db.countFollowers(param4!![0])}"
        numOfFollowing.text = "${db.countFollowing(param4!![0])}"

        return view
    }

}