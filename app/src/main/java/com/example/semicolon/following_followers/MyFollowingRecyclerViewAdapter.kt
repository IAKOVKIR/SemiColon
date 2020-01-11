package com.example.semicolon.following_followers

//import android.content.Context
//import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.*
//import com.example.semicolon.sqlite_database.DatabaseOpenHelper
//import com.example.semicolon.time.Time
//import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.following_search_friends_following.view.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [ListFollowing.OnListFragmentInteractionListener].
 */
class MyFollowingRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mListener: ListFollowing.OnListFragmentInteractionListener?,
    private val myID: Int/*,
    private val mBitMap: BitmapDrawable*/
) : RecyclerView.Adapter<MyFollowingRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    //private val str: Array<String> = arrayOf("unfollow", "in progress", "follow")
    //private lateinit var db: DatabaseOpenHelper

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
            .inflate(R.layout.following_search_friends_following, parent, false)
        //db = DatabaseOpenHelper(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]
        //holder.mUserImage.setImageDrawable(mBitMap)
        holder.mFirstName.text = item.firstName
        holder.mSecondName.text = item.lastName

        //val time = Time()
        //var bool: Int = db.checkFollower(myID, item.id)

        holder.mFollowUnFollowButton.text = "---"//str[bool + 1]
        /*holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == 1) {
                holder.mFollowUnFollowButton.text = str[1]
                bool = 0

                runBlocking {
                    launch(Dispatchers.Default) {
                        val friend = Friend(
                            db.countFriendTable(), myID, item.id,
                            time.getDate(), time.getTime(), 0
                        )
                        db.insertRequest(friend)
                    }
                }

            } else {
                holder.mFollowUnFollowButton.text = str[2]
                bool = 1
                runBlocking {
                    launch(Dispatchers.Default) {
                        db.deleteFollowing(myID, item.id)
                    }
                }
            }
        }*/

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        //val mUserImage: CircleImageView = mView.userImage
        val mFirstName: TextView = mView.first_name
        val mSecondName: TextView = mView.last_name
        val mFollowUnFollowButton: Button = mView.un_follow_button

        override fun toString(): String {
            return super.toString() + " '" + mSecondName.text + "'"
        }
    }
}