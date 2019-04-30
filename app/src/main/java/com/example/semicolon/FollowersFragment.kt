package com.example.semicolon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FollowersFragment : Fragment() {

    private var columnCount = 1
    private var param1 : ArrayList<String>? = null
    private var data: DatabaseOpenHelper? = null
    private var listener: OnListFragmentInteractionListener? = null

    private var tabLayout: TabLayout? = null
    //private var adapter: SectionsPagerAdapter? = null
    private var adapter: FollowersSliderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
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
        adapter = FollowersSliderAdapter(view.context, data!!, listener as FollowersSliderAdapter.OnListFragmentInteractionListener, param1 as ArrayList<String>)

        val vp = view.findViewById<ViewPager>(R.id.viewpager)

        vp.adapter = adapter
        vp.addOnPageChangeListener(pageChangeListener)

        tabLayout = view.findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(vp)
        tabLayout!!.setBackgroundColor(Color.BLACK)
        tabLayout!!.setTabTextColors(ContextCompat.getColor(view.context, R.color.GREY), ContextCompat.getColor(view.context, R.color.WHITE))
        tabLayout!!.getTabAt(0)!!.text = "Followers"
        tabLayout!!.getTabAt(1)!!.text = "Requests"

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

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        /*@JvmStatic
        fun newInstance(columnCount: Int) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
          */
    }

}