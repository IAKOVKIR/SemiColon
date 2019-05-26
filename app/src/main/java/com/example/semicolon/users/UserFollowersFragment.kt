package com.example.semicolon.users

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.semicolon.*
import com.example.semicolon.semi_database.DatabaseOpenHelper

class UserFollowersFragment : Fragment() {

    private var param1: ArrayList<String>? = null
    private var param2: Int? = null
    private var data: DatabaseOpenHelper? = null
    private var listener: OnListFragmentInteractionListener? = null

    private var tabLayout: TabLayout? = null
    private var adapter: FollowersSliderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getStringArrayList("user")
            param2 = it.getInt("id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_tab_layout, container, false)
        data = context?.let { DatabaseOpenHelper(it) }

        //adapter = SectionsPagerAdapter(fragmentManager as FragmentManager)
        adapter = FollowersSliderAdapter(view.context, data!!, listener as FollowersSliderAdapter.OnListFragmentInteractionListener, param1 as ArrayList<String>, 1, param2!!)

        val vp = view.findViewById<ViewPager>(R.id.viewpager)

        vp.adapter = adapter
        vp.addOnPageChangeListener(pageChangeListener)

        tabLayout = view.findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(vp)
        tabLayout!!.setTabTextColors(ContextCompat.getColor(view.context, R.color.GREY), ContextCompat.getColor(view.context, R.color.WHITE))
        tabLayout!!.getTabAt(0)!!.text = "Followers"

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
                val args = Bundle()
                args.putStringArrayList("user", param1)

                val fragment: Fragment = MainFragment()
                fragment.arguments = args
                val manager = fragmentManager
                val transaction = manager!!.beginTransaction()
                transaction.remove(this)
                transaction.commit()

                true

            } else
                false
        }
    }

    private var pageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {}
    }

    /*companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }*/

}