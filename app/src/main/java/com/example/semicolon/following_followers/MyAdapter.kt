package com.example.semicolon.following_followers

//import android.graphics.drawable.BitmapDrawable
//import de.hdodenhof.circleimageview.CircleImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.followers_requests_friends_requests.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyAdapter(private val mValues: List<User>,
                private val mUser: Int/*,
                private val mBitMap: BitmapDrawable*/
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private val str: Array<String> = arrayOf("Confirmed", "Declined")
    private lateinit var db: DatabaseOpenHelper
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //val mUserImage: CircleImageView = view.userImage
        val mFirstName: TextView = view.first_name
        val mLastName: TextView = view.last_name
        val mConfirmButton: Button = view.confirm_button
        val mDeclineButton: Button = view.decline_button
        val mRequestResult: LinearLayout = view.request_result
        val mRequestButtons: LinearLayout = view.request_buttons
        val mResultText: TextView = view.button_result

        override fun toString(): String {
            return super.toString() + " '" + mLastName.text + "'"
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create a new view
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.followers_requests_friends_requests, parent, false)
        db = DatabaseOpenHelper(parent.context)
        // set the view's size, margins, padding's and layout parameters
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: User = mValues[position]
        //holder.mUserImage.setImageDrawable(mBitMap)
        holder.mFirstName.text = item.firstName
        holder.mLastName.text = item.lastName

        holder.mConfirmButton.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {

                var res = false

                withContext(Dispatchers.Default) {
                    res = db.updateRequest(item.id, mUser, -1)
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
                    res = db.deleteFollowingRequest(item.id, mUser)
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
    }

    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount() = mValues.size
}