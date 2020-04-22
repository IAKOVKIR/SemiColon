package com.example.semicolon.following_followers

//import android.graphics.drawable.BitmapDrawable
//import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.support_features.Time
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.followers_requests_friends_requests.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestsRecyclerViewAdapter(
    private val mValues: ArrayList<User>,
    private val mListener: RequestsFragment.OnListFragmentInteractionListener?,
    private val myID: Int
) : RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder>() {

    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")
    private var db: DatabaseOpenHelper? = null
    private val mOnClickListener: View.OnClickListener
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapDrawable: BitmapDrawable
    lateinit var view: View

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item: User = v.tag as User
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.followers_requests_friends_requests, parent, false)
        db = DatabaseOpenHelper(parent.context)
        // set the view's size, margins, padding's and layout parameters
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]
        holder.mUsername.text = item.username

        val time = Time()
        var bool: Int = db!!.checkFollower(myID, item.userId)

        holder.mFollowUnFollowButton.text = str[bool + 1]
        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == -1) {

                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        val friend = Friend(
                            db!!.countFriendTable(), myID, item.userId,
                            time.getDate(), time.getTime(), 0
                        )
                        res = db!!.insertRequest(friend)
                    }

                    launch (Dispatchers.Main) {
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
                        res = db!!.deleteFollowing(myID, item.userId)
                    }

                    launch (Dispatchers.Main) {
                        if (res) {
                            holder.mFollowUnFollowButton.text = str[2]
                            bool = 1
                        }
                    }
                }

            }
        }

        holder.mConfirmButton.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {

                var res = false

                withContext(Dispatchers.Default) {
                    res = db!!.updateRequest(item.userId, myID, 1)
                }

                launch (Dispatchers.Main) {
                    if (res) {
                        holder.mRequestButtons.visibility = View.GONE
                        holder.mFollowUnFollowButton.visibility = View.VISIBLE
                    }
                }
            }
        }

        holder.mDeclineButton.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {

                var res = false

                withContext(Dispatchers.Default) {
                    res = db!!.deleteFollowingRequest(item.userId, myID)
                }

                launch (Dispatchers.Main) {
                    if (res) {
                        holder.mRequestButtons.visibility = View.GONE
                        holder.mFollowUnFollowButton.visibility = View.VISIBLE
                    }
                }
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            withContext(Dispatchers.Default) {
                bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.smithers)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val dif: Double = height.toDouble() / width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(view.context!!.resources, bitmap)
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

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = mValues.size

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mUserImage: CircleImageView = view.userImage
        val mUsername: TextView = mView.username
        val mConfirmButton: TextView = mView.confirm_button
        val mDeclineButton: TextView = mView.decline_button
        val mFollowUnFollowButton: TextView = mView.follow_un_follow_button
        val mRequestButtons: LinearLayout = mView.request_buttons

        override fun toString(): String {
            return super.toString() + " '${mUsername.text}'"
        }
    }
}