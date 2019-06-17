package com.example.semicolon.following_search

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.R
import com.example.semicolon.User

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFollowing.OnListFragmentInteractionListener] interface.
 */

class ListFollowing : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var senderID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            senderID = it.getInt("sender_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.following_search_list_following, container, false)
        val list: RecyclerView = view.findViewById(R.id.list)
        val searchFollowing: EditText = view.findViewById(R.id.search)
        val db = DatabaseOpenHelper(context!!)
        var userList: ArrayList<User> = db.readAllFollowing(senderID!!, senderID!!)

        var bitmap: Bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.smithers)
        val height: Int = bitmap.height
        val width: Int = bitmap.width
        val dif: Double = height.toDouble() / width
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        val bitmapDrawable = BitmapDrawable(context!!.resources, bitmap)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyFollowingRecyclerViewAdapter(
                userList,
                listener as OnListFragmentInteractionListener,
                senderID!!, bitmapDrawable)
        }

        searchFollowing.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val line: String = searchFollowing.text.toString()
                userList = db.searchAllFollowing(senderID!!, line)

                with(list) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MySearchUserRecyclerViewAdapter(
                        userList,
                        listener as ListSearchUser.OnListFragmentInteractionListener,
                         0,bitmapDrawable)
                }
            }
        })

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
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }
}