package com.example.semicolon.following_followers

//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper

class RequestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myID: Int? = null
    private var exceptionID: Int? = null

    val MY_ID = "my_id"
    val USER_ID = "user_id"
    val EXCEPTION_ID = "exception_id"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.followers_requests_list_requests, container, false)

        myID = arguments!!.getInt(MY_ID)
        exceptionID = arguments!!.getInt(EXCEPTION_ID)

        val backButton: ImageView = view.findViewById(R.id.back_button)
        val db = DatabaseOpenHelper(context!!)
        val listUser: ArrayList<User> = db.readAllFollowers(myID!!, 0, exceptionID!!)

        //var bitmap: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.smithers)
        //val height: Int = bitmap.height
        //val width: Int = bitmap.width
        //val dif: Double = height.toDouble() / width
        //bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        //val bitmapDrawable = BitmapDrawable(applicationContext.resources, bitmap)

        viewManager = LinearLayoutManager(context)
        viewAdapter = MyAdapter(listUser, myID!!/*, bitmapDrawable*/)

        recyclerView = view.findViewById<RecyclerView>(R.id.list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        return view
    }


}