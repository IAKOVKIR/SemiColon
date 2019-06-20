package com.example.semicolon.following_followers

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper

class RequestsActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myID: Int? = null
    private var exceptionID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.followers_requests_list_requests)

        myID = intent.getStringExtra("my_id").toInt()
        exceptionID = intent.getStringExtra("exception_id").toInt()

        val backButton: ImageButton = findViewById(R.id.back_button)
        val db = DatabaseOpenHelper(applicationContext)
        val listUser: ArrayList<User> = db.readAllFollowers(myID!!, 0, exceptionID!!)

        var bitmap: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.smithers)
        val height: Int = bitmap.height
        val width: Int = bitmap.width
        val dif: Double = height.toDouble() / width
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        val bitmapDrawable = BitmapDrawable(applicationContext.resources, bitmap)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(listUser, myID!!, bitmapDrawable)

        recyclerView = findViewById<RecyclerView>(R.id.list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        backButton.setOnClickListener { finish() }
    }
}