package com.example.semicolon.semi_following_search

import android.content.Context
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.example.semicolon.FriendActivity
import com.example.semicolon.Login
import com.example.semicolon.R
import com.example.semicolon.User
import java.util.ArrayList

class FollowingActivity : FragmentActivity(), ListFollowing.OnListFragmentInteractionListener {

    private var log = Login()
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onListFragmentInteraction(item: User?) {
        val n = getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
        val list = arrayListOf(n.getString(log.prefVar[0], "") as String,
            n.getString(log.prefVar[1], "") as String, n.getString(log.prefVar[2], "") as String,
            n.getString(log.prefVar[3], "") as String, n.getString(log.prefVar[4], "") as String,
            n.getString(log.prefVar[5], "") as String, n.getString(log.prefVar[6], "") as String,
            n.getString(log.prefVar[7], "") as String)

        val intent = Intent(this, FriendActivity::class.java)
        intent.putExtra("param1", list[0])
        intent.putStringArrayListExtra("param2",item!!.getList())
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        val intent = intent
        val list: ArrayList<String> = intent.getStringArrayListExtra("dataToTheList")
        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager!!, list[0])

        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager, sender_id: String) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val args = Bundle()
        args.putString("senderID", sender_id)

        val listFragment = ListFollowing()
        listFragment.arguments = args

        adapter.addFragment(listFragment, "Following")
        adapter.addFragment(Fragment(), "Find a follower")
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