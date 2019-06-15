package com.example.semicolon.followers_requests

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

class FollowersActivity : FragmentActivity(), ListFollowers.OnListFragmentInteractionListener,
    ListRequests.OnListFragmentInteractionListener {

    private var myID: Int? = null

    override fun onListFragmentInteraction(item: User?) {
        val intent = Intent(this, FriendActivity::class.java)
        intent.putExtra("MyID", myID)
        intent.putExtra("UserID",item!!.id)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)

        myID = intent.getStringExtra("MyID").toInt()
        val viewPager: ViewPager = findViewById(R.id.viewpager)
        val tabLayout: TabLayout = findViewById(R.id.tabs)

        tabLayout.setBackgroundColor(Color.WHITE)
        tabLayout.setTabTextColors(ContextCompat.getColor(applicationContext, R.color.SPECIAL), ContextCompat.getColor(applicationContext, R.color.BLUE))
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

        setupViewPager(viewPager, myID!!)
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager, my_receiver_id: Int) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val args = Bundle()
        args.putInt("receiver_id", my_receiver_id)

        val listFollowers = ListFollowers()
        val listRequests = ListRequests()
        listFollowers.arguments = args
        listRequests.arguments = args

        adapter.addFragment(listFollowers, "Followers")
        adapter.addFragment(listRequests, "Requests")
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