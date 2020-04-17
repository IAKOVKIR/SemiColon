package com.example.semicolon.following_followers

//import android.graphics.drawable.BitmapDrawable
//import de.hdodenhof.circleimageview.CircleImageView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.followers_requests_friends_requests.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdapter(
    private val mValues: ArrayList<User>,
    private val mListener: RequestsFragment.OnListFragmentInteractionListener?,
    private val mUser: Int
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private val str: Array<String> = arrayOf("Confirmed", "Declined")
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

        holder.mConfirmButton.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {

                var res = false

                withContext(Dispatchers.Default) {
                    res = db!!.updateRequest(item.userId, mUser, -1)
                }

                launch (Dispatchers.Main) {
                    if (res) {
                        holder.mResultText.text = str[0]
                        holder.mRequestButtons.visibility = View.GONE
                        holder.mRequestResult.visibility = View.VISIBLE
                    }
                }
            }
        }

        holder.mDeclineButton.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {

                var res = false

                withContext(Dispatchers.Default) {
                    res = db!!.deleteFollowingRequest(item.userId, mUser)
                }

                launch (Dispatchers.Main) {
                    if (res) {
                        holder.mResultText.text = str[1]
                        holder.mRequestButtons.visibility = View.GONE
                        holder.mRequestResult.visibility = View.VISIBLE
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
        val mRequestResult: LinearLayout = mView.request_result
        val mRequestButtons: LinearLayout = mView.request_buttons
        val mResultText: TextView = mView.button_result

        override fun toString(): String {
            return super.toString() + " '${mUsername.text}'"
        }
    }
}