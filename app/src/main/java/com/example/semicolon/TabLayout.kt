package com.example.semicolon

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TabLayout : Fragment() {

    private var columnCount = 1
    private var param1 : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(FollowingFragment.ARG_COLUMN_COUNT)
            param1 = it.getStringArrayList("user")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_tab_layout, container, false)

        val adapter = SectionsPagerAdapter(fragmentManager as FragmentManager)
        val args = Bundle()

        val fragment: Fragment = FollowingFragment()
        args.putStringArrayList("user", param1)
        fragment.arguments = args

        adapter.addFragment(fragment)
        adapter.addFragment(Fragment())

        val vp = view.findViewById<ViewPager>(R.id.viewpager)

        vp.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(vp)
        tabLayout.setBackgroundColor(Color.BLACK)
        tabLayout.setTabTextColors(resources.getColor(R.color.GREY), resources.getColor(R.color.WHITE))

        tabLayout.getTabAt(0)!!.text = "Following"
        tabLayout.getTabAt(1)!!.text = "Search"

        return view
    }

    class SectionsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

        private val mFragmentList : MutableList<Fragment> = ArrayList()

        override fun getItem(p0: Int): Fragment {
            return mFragmentList[p0]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }

    }

}