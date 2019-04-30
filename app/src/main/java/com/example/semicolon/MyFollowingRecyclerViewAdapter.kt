package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.FollowingFragment.OnListFragmentInteractionListener
import de.hdodenhof.circleimageview.CircleImageView

import kotlinx.android.synthetic.main.fragment_friends.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyFollowingRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mContext: Context,
    private val mListener: OnListFragmentInteractionListener?
    //private val mUser: String
    //private val follower: Boolean
) : RecyclerView.Adapter<MyFollowingRecyclerViewAdapter.ViewHolder>() {

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.firstName
        holder.mContentView.text = item.lastName
        val yourID = n!!.getString(Login().prefVar[0], "") as String
        var bool = db!!.checkFollower(yourID, item.id.toString())

        val bitmap = BitmapFactory.decodeResource(holder.mView.resources, R.drawable.smithers)
        val height = bitmap.height
        val width = bitmap.width
        val dif = height.toDouble() / width
        holder.mUserImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, (200 * dif).toInt(), true))

        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate = sdf.format(c.time).trim()

        when (bool) {
            1 -> holder.mFollowUnFollowButton.text = "unfollow"
            2 -> holder.mFollowUnFollowButton.text = "follow"
            else -> holder.mFollowUnFollowButton.text = "in progress"
        }

        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == 2) {
                holder.mFollowUnFollowButton.text = "in progress"
                bool = 3
                val friend = Friend(db!!.countFriendTable(), yourID.toInt(), item.id,
                    strDate.substring(11, 19), strDate.substring(0, 10), bool)
                db!!.insertRequest(friend)
            } else {
                holder.mFollowUnFollowButton.text = "follow"
                bool = 2
                db!!.deleteFollowing(yourID, item.id.toString())
            }
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.friend_first_name
        val mUserImage: CircleImageView = mView.userImage
        val mContentView: TextView = mView.friend_second_name
        val mFollowUnFollowButton: Button = mView.un_follow_button

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}