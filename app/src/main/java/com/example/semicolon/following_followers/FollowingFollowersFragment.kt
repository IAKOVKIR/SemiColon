package com.example.semicolon.following_followers

import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.semicolon.*
import com.example.semicolon.databinding.FollowingFollowersFragmentBinding
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g MY_ID, USER_ID, EXCEPTION_ID and SLIDE_NUMBER
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"
private const val EXCEPTION_ID = "exception_id"
private const val SLIDE_NUMBER = "slide_number"

class FollowingFollowersFragment : Fragment() {

    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private var linePos: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID) //myID
            userID = it.getInt(USER_ID) //myID
            exceptionID = it.getInt(EXCEPTION_ID) //myID
            linePos = it.getInt(SLIDE_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FollowingFollowersFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.following_followers_fragment, container, false)
        val db = DatabaseOpenHelper(requireContext())

        val viewpager: ViewPager2 = binding.viewpager
        val tabs: TabLayout = binding.tabs
        val backButton: TextView = binding.backButton
        val requestsButton: TextView = binding.requestsButton

        tabs.setBackgroundColor(Color.WHITE)
        tabs.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.SPECIAL), ContextCompat.getColor(requireContext(), R.color.BLUE))
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

        val viewPagerAdapter = ViewPagerAdapter(this, myID!!, userID!!, exceptionID!!)
        var selectedTabPosition = 0
        viewpager.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectedTabPosition = position
                }
            })
            currentItem = selectedTabPosition
        }

        // New way of interaction with TabLayout and page title setting
        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                else -> "Following"
            }
        }.attach()

        if (linePos == 1)
            tabs.getTabAt(1)!!.select()

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_followers_following", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        CoroutineScope(Dispatchers.Default).launch {

            var numOfRequests = 0

            withContext(Dispatchers.Default) {
                numOfRequests = db.countFollowingRequests(myID!!)
            }

            launch(Dispatchers.Main) {
                requestsButton.text = "$numOfRequests"
                requestsButton.isEnabled = true
                requestsButton.visibility = View.VISIBLE
            }

        }

        requestsButton.setOnClickListener {

            val fragment = RequestsFragment()
            val argument = Bundle()
            argument.putInt(MY_ID, myID!!)
            argument.putInt(USER_ID, userID!!)
            argument.putInt(EXCEPTION_ID, exceptionID!!)
            fragment.arguments = argument
            parentFragmentManager
                .beginTransaction()
                .addToBackStack("to_followers_requests")
                .replace(R.id.nav_host, fragment, "to_followers_requests")
                .commit()
        }

        return binding.root
    }

    private fun getTab(my_id: Int, user_id: Int, exception_id: Int, pos: Int): Fragment {
        val args = Bundle()
        args.putInt(MY_ID, my_id)
        args.putInt(USER_ID, user_id)
        args.putInt(EXCEPTION_ID, exception_id)
        val f = if (pos == 0)
            ListFollowers()
        else
            ListFollowing()
        f.arguments = args
        return f
    }

    internal inner class ViewPagerAdapter(fr: Fragment, my_id: Int, user_id: Int, exception_id: Int) : FragmentStateAdapter(fr) {

        private val myID = my_id
        private val userID = user_id
        private val exceptionID = exception_id

        override fun createFragment(position: Int): Fragment = when (position) {
            0, 1 -> getTab(myID, userID, exceptionID, position)
            else -> throw IllegalStateException("Invalid adapter position")
        }
        override fun getItemCount(): Int = 2
    }
}