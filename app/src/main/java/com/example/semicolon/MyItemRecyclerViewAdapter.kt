package com.example.semicolon

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.semicolon.ListFragment.OnListFragmentInteractionListener
import com.example.semicolon.databinding.FragmentItemBinding
import com.example.semicolon.models.EventContent.Event
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * [RecyclerView.Adapter] that can display a [Event] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyItemRecyclerViewAdapter(
    private val mValues: ArrayList<Event>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item: Event = v.tag as Event
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FragmentItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.fragment_item, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Event = mValues[position]
        holder.eventName.text = item.eventName
        holder.eventMaxAttendees.text = holder.mView.resources.getString(R.string.event_max_attendees, 0, item.maxAttendees)
        holder.eventLocation.text = item.location
        holder.eventDateView.text = holder.mView.resources.getString(R.string.event_date_or_time, item.startDate, item.endDate)
        holder.eventTimeView.text = holder.mView.resources.getString(R.string.event_date_or_time, item.startTime, item.endTime)
        holder.eventMaxAttendees.setTextColor(Color.parseColor("#00c853"))

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(holder.mView.resources, R.drawable.burns)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val sW: Double = Resources.getSystem().displayMetrics.widthPixels.toDouble()
                var finalWidth: Double = width.toDouble()
                var finalHeight: Double = height.toDouble()

                if (width > (sW * 0.40)) {
                    finalWidth = sW * 0.40
                    finalHeight *= (finalWidth / width)
                }

                if (finalHeight > sW * 0.26) {
                    finalWidth *= ((sW * 0.26) / finalHeight)
                    finalHeight = sW * 0.26
                }

                bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth.toInt(), finalHeight.toInt(), true)
                bitmapDrawable = BitmapDrawable(holder.mView.context!!.resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                holder.mImageView.setImageDrawable(bitmapDrawable)
            }

        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImageView : ImageView = mView.image
        val eventName: TextView = mView.event_name
        val eventMaxAttendees: TextView = mView.event_max_attendees
        val eventLocation: TextView = mView.event_location
        val eventDateView: TextView = mView.event_date
        val eventTimeView: TextView = mView.event_time
    }
}