package com.example.semicolon.followers_requests

import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_friends.view.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [ListFollowers.OnListFragmentInteractionListener].
 */
class MyFollowersRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mListener: ListFollowers.OnListFragmentInteractionListener?,
    private val mBitMap: BitmapDrawable
) : RecyclerView.Adapter<MyFollowersRecyclerViewAdapter.ViewHolder>() {

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
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.followers_requests_friends_followers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mUserImage.setImageDrawable(mBitMap)
        holder.mIdView.text = item.firstName
        holder.mContentView.text = item.lastName

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mUserImage: CircleImageView = mView.userImage
        val mIdView: TextView = mView.first_name
        val mContentView: TextView = mView.last_name

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}