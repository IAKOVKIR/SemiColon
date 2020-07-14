package com.example.semicolon.following_search

import androidx.recyclerview.widget.RecyclerView
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
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.sqlite_database.User
import kotlinx.android.synthetic.main.following_search_friends_search.view.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [ListSearchUser.OnListFragmentInteractionListener].
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
            val item: User = v.tag as User
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
        holder.mIdView.text = item.fullName
        var bool: Int = db!!.checkFollower(myID, item.userId)

        holder.mUserImage.setImageDrawable(mBitMap)

        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate: String = sdf.format(c.time).trim()

        when (bool) {
            1 -> holder.mFollowUnFollowButton.text = str[4]
            2 -> holder.mFollowUnFollowButton.text = str[3]
            else -> holder.mFollowUnFollowButton.text = str[2]
        }

        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == 2) {
                holder.mFollowUnFollowButton.text = str[2]
                bool = 3
                val friend = Follower(
                    db!!.countFriendTable(), myID, item.userId, 0,
                    strDate, strDate
                )
                db!!.insertRequest(friend)
            } else {
                holder.mFollowUnFollowButton.text = str[3]
                bool = 2
                db!!.deleteFollowing(myID, item.userId)
            }
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.user_full_name
        val mUserImage: CircleImageView = mView.userImage
        val mFollowUnFollowButton: Button = mView.un_follow_button

        override fun toString(): String {
            return super.toString() + " '" + mIdView.text + "'"
        }
    }
}