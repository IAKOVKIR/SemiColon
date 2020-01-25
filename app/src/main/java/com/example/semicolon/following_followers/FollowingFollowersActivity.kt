package com.example.semicolon.following_followers

import android.content.Intent
import android.graphics.Color
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.semicolon.*
import kotlinx.coroutines.*
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import java.util.ArrayList

class FollowingFollowersActivity : FragmentActivity(), ListFollowers.OnListFragmentInteractionListener,
    ListFollowing.OnListFragmentInteractionListener {

    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private var job: Job = Job()

    override fun onListFragmentInteraction(item: User?) {
        val intent = Intent(this, FriendActivity::class.java)
        intent.putExtra("my_id", myID!!)
        intent.putExtra("user_id", item!!.id)
        intent.putExtra("exception_id", exceptionID!!)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        val db = DatabaseOpenHelper(applicationContext)
        myID = intent.getStringExtra("my_id")!!.toInt()
        userID = intent.getStringExtra("user_id")!!.toInt()
        exceptionID = intent.getStringExtra("exception_id")!!.toInt()
        val linePos: Int = intent.getIntExtra("string", 0)
        Log.i("check", "$linePos")

        val viewPager: ViewPager = findViewById(R.id.viewpager)
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val backButton: TextView = findViewById(R.id.back_button)
        val requestsButton: TextView = findViewById(R.id.requests_button)

        tabLayout.setBackgroundColor(Color.WHITE)
        tabLayout.setTabTextColors(ContextCompat.getColor(applicationContext, R.color.SPECIAL), ContextCompat.getColor(applicationContext, R.color.BLUE))
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

        setupViewPager(viewPager, myID!!, userID!!, exceptionID!!)
        tabLayout.setupWithViewPager(viewPager)

        if (linePos == 1)
            tabLayout.getTabAt(1)!!.select()

        backButton.setOnClickListener { finish() }

        requestsButton.text = "0"

        job = CoroutineScope(Dispatchers.Default).launch {

            var numOfRequests = 0

            withContext(Dispatchers.Default) {
                numOfRequests = db.countFollowingRequests(myID!!)
            }

            launch (Dispatchers.Main) {
                requestsButton.text = "$numOfRequests"
            }

        }

        requestsButton.setOnClickListener {
            val intent = Intent(this, RequestsActivity::class.java)
            intent.putExtra("my_id", myID!!.toString())
            //intent.putExtra("user_id", userID!!)
            intent.putExtra("exception_id", exceptionID.toString())
            startActivity(intent)
        }

    }

    private fun setupViewPager(viewPager: ViewPager, my_id: Int, user_id: Int, exception_id: Int) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val args = Bundle()
        args.putInt("my_id", my_id)
        args.putInt("user_id", user_id)
        args.putInt("exception_id", exception_id)

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
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

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