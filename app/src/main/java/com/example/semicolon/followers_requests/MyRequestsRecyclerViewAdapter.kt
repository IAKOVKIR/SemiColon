package com.example.semicolon.followers_requests

import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.semicolon.*
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.followers_requests_friends_requests.view.*

/**
 * [RecyclerView.Adapter] that can display a [Friend] and makes a call to the
 * specified [ListRequests.OnListFragmentInteractionListener].
 */
class MyRequestsRecyclerViewAdapter(
    private val mValues: List<User>,
    private val mListener: ListRequests.OnListFragmentInteractionListener?,
    private val mUser: Int,
    private val mBitMap: BitmapDrawable
) : RecyclerView.Adapter<MyRequestsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val str: Array<String> = arrayOf("Confirmed", "Declined")
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.followers_requests_friends_requests, parent, false)
        db = DatabaseOpenHelper(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mUserImage.setImageDrawable(mBitMap)
        holder.mFirstName.text = item.firstName
        holder.mLastName.text = item.lastName

        holder.mConfirmButton.setOnClickListener {
            db!!.updateRequest(item.id, mUser, -1)
            holder.mResultText.text = str[0]
            holder.mRequestButtons.visibility = View.GONE
            holder.mRequestResult.visibility = View.VISIBLE
        }

        holder.mDeclineButton.setOnClickListener {
            db!!.deleteUser(mUser)
            holder.mResultText.text = str[1]
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
        val mUserImage: CircleImageView = mView.userImage
        val mFirstName: TextView = mView.first_name
        val mLastName: TextView = mView.last_name
        val mConfirmButton: Button = mView.confirm_button
        val mDeclineButton: Button = mView.decline_button
        val mRequestResult: LinearLayout = mView.request_result
        val mRequestButtons: LinearLayout = mView.request_buttons
        val mResultText: TextView = mView.button_result

        override fun toString(): String {
            return super.toString() + " '" + mLastName.text + "'"
        }
    }
}