package com.example.semicolon.following_followers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.ListAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.databinding.RequestsRecyclerViewAdapterBinding
import com.example.semicolon.following_followers.view_models.RequestsFragmentViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.support_features.Time
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.requests_recycler_view_adapter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestsRecyclerViewAdapter(private val viewModel: RequestsFragmentViewModel, private val mValues: ArrayList<User>) : RecyclerView.Adapter<
        RequestsRecyclerViewAdapter.ViewHolder>(/*UserDiffCallback()*/) {

    //private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues[position], viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: RequestsRecyclerViewAdapterBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: User, viewModel: RequestsFragmentViewModel) {
            binding.user = item
            binding.username.text = item.username

            binding.ifitworkssasi.setOnClickListener {
                viewModel.onUserClicked(item.userId)
            }

            binding.confirmButton.setOnClickListener {
                viewModel.setNewCondition(item.userId, 1)
                confirmOrDecline()
            }

            binding.declineButton.setOnClickListener {
                viewModel.deleteRecord(item.userId)
                confirmOrDecline()
            }
        }

        private fun confirmOrDecline() {
            binding.apply {
                confirmButton.visibility = View.GONE
                declineButton.visibility = View.GONE
                followUnFollowButton.visibility = View.VISIBLE
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RequestsRecyclerViewAdapterBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    // Replace the contents of a view (invoked by the layout manager)
    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: User = mValues[position]
        holder.mUsername.text = item.username

        val time = Time()
        var bool: Int = db.checkFollower(myID, item.userId)

        holder.mFollowUnFollowButton.text = str[bool + 1]
        holder.mFollowUnFollowButton.setOnClickListener {
            if (bool == -1) {

                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        val friend = Follower(
                            db.countFriendTable(), myID, item.userId, 0,
                            time.toString(), time.toString()
                        )
                        res = db.insertRequest(friend)
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
                        res = db.deleteFollowing(myID, item.userId)
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

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(holder.mView.resources, R.drawable.smithers)
                val dif: Double = bitmap.height.toDouble() / bitmap.width
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
    }*/
}

/*class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}*/