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

class FollowingSliderAdapter(private val context : Context, var data : DatabaseOpenHelper,
                             private var listener: OnListFragmentInteractionListener,
                             var param1 : ArrayList<String>) : PagerAdapter() {

    //arrays with strings for pages
    private var columnCount = 1

    override fun getCount(): Int {
        return 2
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_friends_list, container, false)
        data = DatabaseOpenHelper(context)

        val list = view.findViewById<RecyclerView>(R.id.list)

        when (position) {
            0 -> {
                // Set the adapter
                if (list is RecyclerView)
                    with(list) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = MyFollowingRecyclerViewAdapter(data.readAllFollowing(param1[0]), context,
                            listener as FollowingFragment.OnListFragmentInteractionListener, param1[0])
                    }

            }
            else -> {
                if (list is RecyclerView)
                    with(list) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = MyFollowingRecyclerViewAdapter(listOf(), context,
                            listener as FollowingFragment.OnListFragmentInteractionListener, param1[0])
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