package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.semicolon.databinding.FragmentMainBinding
import com.example.semicolon.semi_settings.SettingFragment
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.following_followers.FollowingFollowersFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*

// the fragment initialization parameters, e.g MY_ID, USER_ID, EXCEPTION_ID and SLIDE_NUMBER
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"
private const val EXCEPTION_ID = "exception_id"
private const val SLIDE_NUMBER = "slide_number"

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var userID: Int = 0
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n: SharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        userID = n.getInt("id", 1)
        username = n.getString("username", "")!!

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(requireContext())

        //TextViews
        //TextView representing user's full name
        val nameText: TextView = binding.name

        //TextView representing user's phone number
        val phoneNum: TextView = binding.phoneNumber

        //TextView representing the number of followers
        val numOfFollowers: TextView = binding.followersNumber

        //TextView representing the number of users you follow
        val numOfFollowing: TextView = binding.followingNumber

        //TextView representing user's email
        val email: TextView = binding.email

        val imageView: CircleImageView = binding.circleImageView

        //LinearLayouts
        val followersLink: LinearLayout = binding.linearLayoutFollowers
        val followingLink: LinearLayout = binding.linearLayoutFollowing

        nameText.text = username

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.burns)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val dif: Double = height.toDouble() / width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(requireContext().resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                imageView.setImageDrawable(bitmapDrawable)
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            var userPhone = ""
            var emailText = ""
            var followers = 0
            var following = 0

            withContext(Dispatchers.Default) {
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
                phoneNum.text = userPhone
                email.text = emailText
                numOfFollowers.text = "$followers"
                numOfFollowing.text = "$following"
            }

        }

        followersLink.setOnClickListener {
            sendToFollowersFollowing(0)
        }

        followingLink.setOnClickListener {
            sendToFollowersFollowing(1)
        }

        val settingsButton : ImageButton = binding.settingsButton
        settingsButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack("to_settings")
                .add(R.id.nav_host, SettingFragment(), "to_settings")
                .commit()
        }

        //Inflate the layout for this fragment
        return binding.root
    }

    private fun sendToFollowersFollowing(slideNumber: Int) {
        val fragment = FollowingFollowersFragment()
        val argument = Bundle()
        argument.putInt(MY_ID, userID)//myID
        argument.putInt(USER_ID, userID)//myID
        argument.putInt(EXCEPTION_ID, userID)//myID
        argument.putInt(SLIDE_NUMBER, slideNumber)
        fragment.arguments = argument
        parentFragmentManager
            .beginTransaction()
            .addToBackStack("to_followers_following")
            .replace(R.id.nav_host, fragment, "to_followers_following")
            .commit()
    }
}