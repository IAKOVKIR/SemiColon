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
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.semicolon.databinding.FragmentMainBinding
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var userID: Int = -1
    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n: SharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        userID = n.getInt("id", -1)
        username = n.getString("username", "")!!
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(requireContext())

        //TextView representing username
        binding.name.text = username

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.burns)
                val dif: Double = bitmap.height.toDouble() / bitmap.width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(requireContext().resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                // CircleViewImage
                binding.circleImageView.setImageDrawable(bitmapDrawable)
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
                //TextView representing user's phone number
                binding.phoneNumber.text = userPhone

                //TextView representing user's email
                binding.email.text = emailText

                //TextView representing the number of followers
                binding.followersNumber.text = "$followers"

                //TextView representing the number of users you follow
                binding.followingNumber.text = "$following"
            }

        }

        //followers layout
        binding.linearLayoutFollowers.setOnClickListener { view: View ->
            view.findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToFollowingFollowersFragment(userID, userID, userID, 0))
        }

        //following layout
        binding.linearLayoutFollowing.setOnClickListener {view: View ->
            view.findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToFollowingFollowersFragment(userID, userID, userID, 1))
        }

        // settings ImageButton
        binding.settingsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        //Inflate the layout for this fragment
        return binding.root
    }
}