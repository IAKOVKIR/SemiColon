package com.example.semicolon.semi_following_search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.semicolon.semi_database.DatabaseOpenHelper
import com.example.semicolon.R
import com.example.semicolon.User

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFollowers.OnListFragmentInteractionListener] interface.
 */

class ListFollowing : Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private var data: DatabaseOpenHelper? = null
    private var senderID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            senderID = it.getString("senderID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_following, container, false)
        val list = view.findViewById<RecyclerView>(R.id.list)
        data = context?.let { DatabaseOpenHelper(it) } as DatabaseOpenHelper

        // Set the adapter
        if (list is RecyclerView)
            with(list) {
                when {
                    columnCount <= 1 -> layoutManager = LinearLayoutManager(context)
                    else -> layoutManager = GridLayoutManager(context, columnCount)
                }
                adapter = MyFollowingRecyclerViewAdapter(
                    data!!.readAllFollowing(senderID!!),
                    context,
                    listener as OnListFragmentInteractionListener,
                    "1",
                    false
                )
            }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}