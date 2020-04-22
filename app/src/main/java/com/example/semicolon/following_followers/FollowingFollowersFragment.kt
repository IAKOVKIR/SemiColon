package com.example.semicolon.following_followers

import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.fragment.app.*
import com.example.semicolon.*
import kotlinx.coroutines.*
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import java.util.ArrayList

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
    private var job: Job = Job()
    private lateinit var viewPager: ViewPager

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
        val view: View = inflater.inflate(R.layout.activity_followers, container, false)

        val db = DatabaseOpenHelper(requireContext())

        viewPager = view.findViewById(R.id.viewpager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        val backButton: TextView = view.findViewById(R.id.back_button)
        val requestsButton: TextView = view.findViewById(R.id.requests_button)

        tabLayout.setBackgroundColor(Color.WHITE)
        tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.SPECIAL), ContextCompat.getColor(requireContext(), R.color.BLUE))
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

        setupViewPager(viewPager, myID!!, userID!!, exceptionID!!)
        tabLayout.setupWithViewPager(viewPager)

        if (linePos == 1)
            tabLayout.getTabAt(1)!!.select()

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_followers_following", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        job = CoroutineScope(Dispatchers.Default).launch {

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

        return view
    }

    private fun setupViewPager(viewPager: ViewPager, my_id: Int, user_id: Int, exception_id: Int) {
        val adapter = ViewPagerAdapter(childFragmentManager)

        val args = Bundle()
        args.putInt(MY_ID, my_id)
        args.putInt(USER_ID, user_id)
        args.putInt(EXCEPTION_ID, exception_id)

        val listFollowers = ListFollowers()
        val listFollowing = ListFollowing()
        listFollowers.arguments = args
        listFollowing.arguments = args

        adapter.addFragment(listFollowers, "Followers")
        adapter.addFragment(listFollowing, "Following")

        viewPager.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var mFragmentList = ArrayList<Fragment>()
        private var mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
                mFragmentList.add(fragment)
                mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}