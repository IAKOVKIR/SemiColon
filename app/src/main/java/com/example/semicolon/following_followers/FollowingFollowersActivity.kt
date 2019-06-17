package com.example.semicolon.following_followers

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.example.semicolon.*
import java.util.ArrayList

class FollowingFollowersActivity : FragmentActivity(), ListFollowers.OnListFragmentInteractionListener,
    ListFollowing.OnListFragmentInteractionListener {

    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private var linePos = ""

    override fun onListFragmentInteraction(item: User?) {
        val intent = Intent(this, FriendActivity::class.java)
        intent.putExtra("my_id", myID!!)
        intent.putExtra("user_id", item!!.id)
        intent.putExtra("exception_id", userID!!)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        myID = intent.getStringExtra("my_id").toInt()
        userID = intent.getStringExtra("user_id").toInt()
        exceptionID = intent.getStringExtra("exception_id").toInt()
        linePos = intent.getStringExtra("string")
        val viewPager: ViewPager = findViewById(R.id.viewpager)
        val tabLayout: TabLayout = findViewById(R.id.tabs)

        tabLayout.setBackgroundColor(Color.WHITE)
        tabLayout.setTabTextColors(ContextCompat.getColor(applicationContext, R.color.SPECIAL), ContextCompat.getColor(applicationContext, R.color.BLUE))
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

        setupViewPager(viewPager, myID!!, userID!!, exceptionID!!)
        tabLayout.setupWithViewPager(viewPager)

        if (linePos == "1")
            tabLayout.getTabAt(1)!!.select()

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

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
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