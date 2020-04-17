package com.example.semicolon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.models.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.followers_requests_friends_followers.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [UserSearchFragment.OnListFragmentInteractionListener].
 */
class MyUserSearchRecyclerViewAdapter(
    private val mValues: ArrayList<User>,
    private val mListener: UserSearchFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyUserSearchRecyclerViewAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_search_fragment_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]


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

        holder.mIdView.text = item.username
        //holder.mContentView.text = item.lastName

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