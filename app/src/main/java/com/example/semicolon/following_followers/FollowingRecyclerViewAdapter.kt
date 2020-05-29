package com.example.semicolon.following_followers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.semicolon.*
import com.example.semicolon.databinding.FollowingRecyclerViewAdapterBinding
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.support_features.Time
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.following_recycler_view_adapter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [RecyclerView.Adapter] that can display a [FriendFragment] and makes a call to the
 * specified [ListFollowing.OnListFragmentInteractionListener].
 */
class FollowingRecyclerViewAdapter(
    private val mValues: ArrayList<User>,
    private val mListener: ListFollowing.OnListFragmentInteractionListener?,
    private val myID: Int
) : RecyclerView.Adapter<FollowingRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow") //-1, 0, 1
    lateinit var db: DatabaseOpenHelper

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item: User = v.tag as User
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FollowingRecyclerViewAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.following_recycler_view_adapter, parent, false)
        db = DatabaseOpenHelper(parent.context)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]
        val time = Time()
        var bool = 0

        holder.mIdView.text = item.username

        if (item.userId != myID) {
            CoroutineScope(Dispatchers.Default).launch {

                withContext(Dispatchers.Default) {
                    bool = db.checkFollower(myID, item.userId)
                }

                launch(Dispatchers.Main) {
                    holder.mFollowUnFollowButton.text = str[bool + 1]
                    holder.mFollowUnFollowButton.setBackgroundResource(R.drawable.reg_square)
                    holder.mFollowUnFollowButton.isEnabled = true
                }
            }

            holder.mIdView.text = item.username
            holder.mFollowUnFollowButton.setOnClickListener {
                if (bool == -1) {

                    CoroutineScope(Dispatchers.Default).launch {

                        var res = false

                        withContext(Dispatchers.Default) {
                            val friend = Friend(
                                myID, item.userId,
                                time.getDate(), time.getTime(), 0
                            )
                            res = db.insertRequest(friend)
                        }

                        launch(Dispatchers.Main) {
                            if (res) {
                                holder.mFollowUnFollowButton.text = str[1]
                                bool = 0
                            }
                        }
                    }

                } else {

                    CoroutineScope(Dispatchers.Default).launch {

                        var res = false

                        withContext(Dispatchers.Default) {
                            res = db.deleteFollowing(myID, item.userId)
                        }

                        launch(Dispatchers.Main) {
                            if (res) {
                                holder.mFollowUnFollowButton.text = str[2]
                                bool = 1
                            }
                        }
                    }

                }
            }
        } else
            holder.mFollowUnFollowButton.visibility = View.GONE

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(holder.mView.resources, R.drawable.smithers)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val dif: Double = height.toDouble() / width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(holder.mView.context!!.resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                holder.mUserImage.setImageDrawable(bitmapDrawable)
            }

        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mUserImage: CircleImageView = mView.userImage
        val mIdView: TextView = mView.username
        val mFollowUnFollowButton: TextView = mView.follow_un_follow_button

        override fun toString(): String {
            return super.toString() + " '${mIdView.text}'"
        }
    }
}