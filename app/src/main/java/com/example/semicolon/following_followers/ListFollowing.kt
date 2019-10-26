package com.example.semicolon.following_followers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    var listUser: ArrayList<User>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt("my_id")
            userID = it.getInt("user_id")
            exceptionID = it.getInt("exception_id")
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
                listUser!!,
                listener as OnListFragmentInteractionListener,
                userID!!, bitmapDrawable)
            setHasFixedSize(true)
        }

        val th = Thread(Runnable {
            val testList: ArrayList<User> = db.readAllFollowing(userID!!, exceptionID!!)

            for (i in 0 until testList.size)
                listUser!!.add(testList[i])

        })

        th.start()

        with(list) {
            adapter!!.notifyDataSetChanged()
        }

        th.join()

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