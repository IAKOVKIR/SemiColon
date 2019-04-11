package com.example.semicolon

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.FollowingFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_friends.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFriendsRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mContext: Context,
    private val mListener: OnListFragmentInteractionListener?,
    private val mUser: String,
    private val buttonsVisibility: Boolean
    /*private val follower: Boolean*/
) : RecyclerView.Adapter<MyFriendsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var db: DatabaseOpenHelper? = null
    private var n: SharedPreferences? = null

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
        n = mContext.getSharedPreferences(Login().prefName, Context.MODE_PRIVATE)
        db = DatabaseOpenHelper(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.firstName
        holder.mContentView.text = item.lastName
        val yourID = n!!.getString(Login().prefVar[0], "") as String
        var bool = db!!.checkFollower(yourID, item.id.toString())

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        when (bool) {
            "confirmed" -> holder.mFollowUnFollowButton.text = "unfollow"
            "" -> holder.mFollowUnFollowButton.text = "follow"
            else -> holder.mFollowUnFollowButton.text = "in progress"
        }

        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == "confirmed") {
                holder.mFollowUnFollowButton.text = "follow"
                bool = ""
                db!!.deleteFollowing(yourID, item.id.toString())
            } else if (bool == "") {
                holder.mFollowUnFollowButton.text = "in progress"
                bool = "inProgress"
                val friend = Friend(db!!.countFriendTable(), yourID.toInt(), item.id,
                    strDate.substring(11, 19), strDate.substring(0, 10), bool)
                db!!.insertRequest(friend)
            }
        }

        if (buttonsVisibility) {
            holder.mRequestButtons.visibility = View.VISIBLE
            holder.mUnFollowButtons.visibility = View.GONE
        } else {
            holder.mRequestButtons.visibility = View.GONE
            holder.mUnFollowButtons.visibility = View.VISIBLE
        }

        holder.mConfirmButton.setOnClickListener {
            db!!.updateRequest(item.id.toString(), mUser, "confirmed")
            holder.mResultText.text = "Confirmed"
            holder.mRequestButtons.visibility = View.GONE
            holder.mRequestResult.visibility = View.VISIBLE
        }

        holder.mDeclineButton.setOnClickListener {
            db!!.deleteUser(mUser)
            holder.mResultText.text = "Declined"
            holder.mRequestButtons.visibility = View.GONE
            holder.mRequestResult.visibility = View.VISIBLE
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.friend_first_name
        val mContentView: TextView = mView.friend_second_name
        val mConfirmButton: Button = mView.confirm_button
        val mDeclineButton: Button = mView.decline_button
        val mRequestResult: LinearLayout = mView.request_result
        val mRequestButtons: LinearLayout = mView.request_buttons
        val mUnFollowButtons: LinearLayout = mView.follow_un_follow_buttons
        val mFollowUnFollowButton: Button = mView.un_follow_button
        val mResultText: TextView = mView.button_result

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}