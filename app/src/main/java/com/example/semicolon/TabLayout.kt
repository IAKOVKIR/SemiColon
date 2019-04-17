package com.example.semicolon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class TabLayout : Fragment() {

    private var columnCount = 1
    private var param1 : ArrayList<String>? = null
    private var data: DatabaseOpenHelper? = null
    private var listener: OnListFragmentInteractionListener? = null

    var tabLayout: TabLayout? = null
    //private var adapter: SectionsPagerAdapter? = null
    private var adapter: FollowingSliderAdapter? = null

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
        data = context?.let { DatabaseOpenHelper(it) } as DatabaseOpenHelper

        //adapter = SectionsPagerAdapter(fragmentManager as FragmentManager)
        adapter = FollowingSliderAdapter(view.context, data!!, listener as FollowingSliderAdapter.OnListFragmentInteractionListener, param1 as ArrayList<String>)
        val args = Bundle()

        val fragment: Fragment = FollowingFragment()
        args.putStringArrayList("user", param1)
        fragment.arguments = args

        /*if (adapter!!.count == 0) {
            adapter!!.addFragment(fragment)
            adapter!!.addFragment(Fragment())
        }*/

        val vp = view.findViewById<ViewPager>(R.id.viewpager)

        vp.adapter = adapter
        vp.addOnPageChangeListener(pageChangeListener)

        tabLayout = view.findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(vp)
        tabLayout!!.setBackgroundColor(Color.BLACK)
        tabLayout!!.setTabTextColors(resources.getColor(R.color.GREY), resources.getColor(R.color.WHITE))
        tabLayout!!.getTabAt(0)!!.text = "Following"
        tabLayout!!.getTabAt(1)!!.text = "Search"

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener)
            listener = context
        else
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

    override fun onResume() {
        super.onResume()
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                tabLayout!!.removeAllTabs()
                adapter = null

                val args = Bundle()
                args.putStringArrayList("user", param1)

                val fragment: Fragment = MainFragment()
                fragment.arguments = args
                val manager = fragmentManager
                val transaction = manager!!.beginTransaction()
                transaction.replace(R.id.nav_host, fragment)
                transaction.remove(Fragment())
                transaction.remove(FollowingFragment())
                // Commit the transaction
                transaction.commit()

                true

            } else
                false
        }
    }


    private var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {

            when (position) {
                0 -> {

                }
                else -> {

                }
            }

        }
    }

    class SectionsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

        private val mFragmentList : MutableList<Fragment> = ArrayList(2)
        private val titles = arrayOf("Following", "Search")


        override fun getItem(p0: Int): Fragment {
            return mFragmentList[p0]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(p0: Int): CharSequence {
            return titles[p0]
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }

    }

}