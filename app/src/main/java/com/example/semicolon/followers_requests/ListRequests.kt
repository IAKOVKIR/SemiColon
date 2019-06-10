package com.example.semicolon.followers_requests

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.R
import com.example.semicolon.User

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListRequests.OnListFragmentInteractionListener] interface.
 */
class ListRequests : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var receiverID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            receiverID = it.getInt("receiver_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.followers_requests_list_requests, container, false)
        val list: RecyclerView = view.findViewById(R.id.list)
        val db = DatabaseOpenHelper(context!!)
        val listUser: ArrayList<User> = db.readAllFollowers(receiverID!!, 0, receiverID!!)

        var bitmap: Bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.smithers)
        val height: Int = bitmap.height
        val width: Int = bitmap.width
        val dif: Double = height.toDouble() / width
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        val bitmapDrawable = BitmapDrawable(context!!.resources, bitmap)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyRequestsRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener,
                receiverID!!, bitmapDrawable)
        }

        db.close()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener)
            listener = context
        else
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }
}