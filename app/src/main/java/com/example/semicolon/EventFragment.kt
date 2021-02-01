package com.example.semicolon

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.semicolon.models.DBContract
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameters
private const val MY_ID = "my_id"
private const val EVENT_ID = "event_id"

/**
 * A simple [Fragment] subclass.
 */
class EventFragment : Fragment() {

    private var myID: Int? = null
    private var eventID: Int? = null

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapDrawable: BitmapDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID)
            eventID = it.getInt(EVENT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_event, container, false)
        val db = DatabaseOpenHelper(requireContext())

        val image: ImageView = view.findViewById(R.id.event_image)
        val eventName: TextView = view.findViewById(R.id.event_name)
        val eventMaxAttendees: TextView = view.findViewById(R.id.event_max_attendees)
        val eventLocation: TextView = view.findViewById(R.id.event_location)

        CoroutineScope(Dispatchers.Default).launch {

            withContext(Dispatchers.Default) {
                bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.burns)
                val height: Int = bitmap.height
                val width: Int = bitmap.width
                val sW: Double = Resources.getSystem().displayMetrics.widthPixels.toDouble()
                var finalWidth: Double = width.toDouble()
                var finalHeight: Double = height.toDouble()

                if (width > sW) {
                    finalWidth = sW
                    finalHeight *= (finalWidth / width)
                }

                if (finalHeight > sW * 0.6) {
                    finalWidth *= ((sW * 0.6) / finalHeight)
                    finalHeight = sW * 0.6
                }

                Log.e("W + H", "${finalWidth.toInt()} + ${finalHeight.toInt()}")

                bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth.toInt(), finalHeight.toInt(), true)
                bitmapDrawable = BitmapDrawable(view.context!!.resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                image.setImageDrawable(bitmapDrawable)
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            var eName: String
            var eMaxAttendees: String
            var eLocation: String

            withContext(Dispatchers.Default) {
                eName = db.getEventsData(eventID!!, DBContract.UserEntry.EVENT_COLUMN_NAME)
                eMaxAttendees = db.getEventsData(eventID!!, DBContract.UserEntry.EVENT_COLUMN_MAX_ATTENDEES)
                eLocation = db.getEventsData(eventID!!, DBContract.UserEntry.EVENT_COLUMN_LOCATION)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                eventName.text = eName
                eventMaxAttendees.text = eMaxAttendees
                eventLocation.text = eLocation
            }

        }

        return view
    }

}