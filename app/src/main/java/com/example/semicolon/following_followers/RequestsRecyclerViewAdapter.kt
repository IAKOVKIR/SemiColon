package com.example.semicolon.following_followers

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.databinding.RequestsRecyclerViewAdapterBinding
import com.example.semicolon.following_followers.view_models.RequestsFragmentViewModel
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestsRecyclerViewAdapter(private val myID: Int, private val viewModel: RequestsFragmentViewModel,
                                  private val mValues: ArrayList<User>, private val application: Application)
    : RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myID, mValues[position], viewModel, application)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: RequestsRecyclerViewAdapterBinding)
        : RecyclerView.ViewHolder(binding.root){
        private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")

        //val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao

        fun bind(myID: Int, item: User, viewModel: RequestsFragmentViewModel, application: Application) {
            binding.user = item
            binding.username.text = item.username

            val time = Time()
            var bool: Int = -1
            val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao
            CoroutineScope(Dispatchers.Default).launch {
                withContext(Dispatchers.Default) {
                    if (followerDataSource.isRecordExist(myID, item.userId) == 1) {
                        bool = followerDataSource.checkFollower(myID, item.userId)
                    }
                }
                launch(Dispatchers.Main) {
                    binding.followUnFollowButton.text = str[bool + 1]
                    binding.followUnFollowButton.isEnabled = true
                }
            }

            binding.followUnFollowButton.setOnClickListener {
                if (bool == -1) {

                    CoroutineScope(Dispatchers.Default).launch {
                        var res = false

                        withContext(Dispatchers.Default) {
                            val friend = Follower(
                                followerDataSource.getMaxId() + 1, myID, item.userId, 0,
                                time.toString(), time.toString()
                            )
                            followerDataSource.insert(friend)
                            if (followerDataSource.isRecordExistWithCondition(myID, item.userId, 0) == 1) {
                                res = true
                            }
                        }

                        launch(Dispatchers.Main) {
                            if (res) {
                                binding.followUnFollowButton.text = str[1]
                                bool = 0
                            }
                        }
                    }

                } else {

                    CoroutineScope(Dispatchers.Default).launch {
                        var res = false

                        withContext(Dispatchers.Default) {
                            followerDataSource.deleteRecord(myID, item.userId)
                            if (followerDataSource.isRecordExist(myID, item.userId) == 0) {
                                res = true
                            }
                        }

                        launch (Dispatchers.Main) {
                            if (res) {
                                binding.followUnFollowButton.text = str[0]
                                bool = -1
                            }
                        }
                    }

                }
            }

            CoroutineScope(Dispatchers.Default).launch {

                lateinit var bitmapDrawable: BitmapDrawable

                withContext(Dispatchers.Default) {
                    var bitmap: Bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.smithers)
                    val dif: Double = bitmap.height.toDouble() / bitmap.width
                    bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                    bitmapDrawable = BitmapDrawable(binding.root.context!!.resources, bitmap)
                }

                launch (Dispatchers.Main) {
                    // process the data on the UI thread
                    binding.userImage.setImageDrawable(bitmapDrawable)
                }

            }

            binding.constraintLayout.setOnClickListener {
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
}