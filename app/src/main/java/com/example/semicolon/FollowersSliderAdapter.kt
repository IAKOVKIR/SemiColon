package com.example.semicolon

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.followers_requests.ListFollowers
import com.example.semicolon.followers_requests.MyFollowersRecyclerViewAdapter

class FollowersSliderAdapter(private val context: Context, var data: DatabaseOpenHelper,
                             private var listener: OnListFragmentInteractionListener,
                             var param1: ArrayList<String>, private val numOfTabs: Int,
                             private val except: Int) : PagerAdapter() {

    //arrays with strings for pages
    private var columnCount = 1

    override fun getCount(): Int {
        return numOfTabs
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_friends_list, container, false)
        data = DatabaseOpenHelper(context)

        val list = view.findViewById<RecyclerView>(R.id.list)

        // Set the adapter
        if (list is RecyclerView) {
            with(list) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = if (position == 0) {
                    MyFollowersRecyclerViewAdapter(
                        data.readAllFollowers(param1[0], 1, except),
                        context,
                        listener as /*FollowersFragment*/ListFollowers.OnListFragmentInteractionListener,
                        param1[0],
                        false
                    )
                } else {
                    MyFollowersRecyclerViewAdapter(
                        data.readAllFollowers(param1[0], 2, except),
                        context,
                        listener as /*FollowersFragment*/ListFollowers.OnListFragmentInteractionListener,
                        param1[0],
                        true
                    )
                }
            }
        }

        container.addView(view)

        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

}