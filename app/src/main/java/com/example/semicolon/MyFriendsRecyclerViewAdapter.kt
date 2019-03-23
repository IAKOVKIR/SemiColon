package com.example.semicolon

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.semicolon.FriendsFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_friends.view.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFriendsRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.firstName
        holder.mContentView.text = item.lastName

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.friend_first_name
        val mContentView: TextView = mView.friend_second_name

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}