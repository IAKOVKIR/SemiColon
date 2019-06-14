package com.example.semicolon.following_search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.*
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.drawable.BitmapDrawable
import kotlinx.android.synthetic.main.following_search_friends_search.view.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [ListFollowing.OnListFragmentInteractionListener].
 */
class MySearchUserRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mListener: ListSearchUser.OnListFragmentInteractionListener?,
    private val myID: Int,
    private val mBitMap: BitmapDrawable
) : RecyclerView.Adapter<MySearchUserRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val str: Array<String> = arrayOf("Confirmed", "Declined", "in progress", "follow", "unfollow")
    private var db: DatabaseOpenHelper? = null

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
            .inflate(R.layout.following_search_friends_search, parent, false)
        db = DatabaseOpenHelper(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]
        holder.mIdView.text = item.firstName
        holder.mContentView.text = item.lastName
        var bool: Int = db!!.checkFollower(myID, item.id)

        holder.mUserImage.setImageDrawable(mBitMap)

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        when (bool) {
            1 -> holder.mFollowUnFollowButton.text = str[4]
            2 -> holder.mFollowUnFollowButton.text = str[3]
            else -> holder.mFollowUnFollowButton.text = str[2]
        }

        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == 2) {
                holder.mFollowUnFollowButton.text = str[2]
                bool = 3
                val friend = Friend(
                    db!!.countFriendTable(), myID, item.id,
                    strDate.substring(11, 19), strDate.substring(0, 10), bool
                )
                db!!.insertRequest(friend)
            } else {
                holder.mFollowUnFollowButton.text = str[3]
                bool = 2
                db!!.deleteFollowing(myID, item.id)
            }
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.first_name
        val mUserImage: CircleImageView = mView.userImage
        val mContentView: TextView = mView.last_name
        val mFollowUnFollowButton: Button = mView.un_follow_button

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}