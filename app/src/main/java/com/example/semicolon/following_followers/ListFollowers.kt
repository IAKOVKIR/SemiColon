package com.example.semicolon.following_followers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.R
import com.example.semicolon.User

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFollowers.OnListFragmentInteractionListener] interface.
 */
class ListFollowers : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapDrawable: BitmapDrawable
    private var listUser: ArrayList<User> = ArrayList()
    lateinit var list: RecyclerView

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
        val view: View = inflater.inflate(R.layout.followers_requests_list_followers, container, false)
        list = view.findViewById(R.id.list)
        val db = DatabaseOpenHelper(context!!)

        Log.i("check", "$exceptionID")
        bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.smithers)
        /*val height: Int = bitmap.height
        val width: Int = bitmap.width
        val dif: Double = height.toDouble() / width
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)*/
        bitmapDrawable = BitmapDrawable(context!!.resources, bitmap)

        //listUser = db.readAllFollowers(myID!!, -1, myID!!)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyFollowersRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener,
                bitmapDrawable)
            setHasFixedSize(true)
        }



        val th = Thread(Runnable {
            var list: ArrayList<User> = ArrayList()
            try {
                list = db.readAllFollowers(userID!!, -1, exceptionID!!)
            }
            catch(e: Exception) {

            }

            for (i in 0 until list.size)
                listUser.add(list[i])

        })

        th.start()

        with(list) {
            adapter!!.notifyDataSetChanged()
        }

        //th.join()

        //WordLoaderTask(listUser!!).execute()

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

    /*internal inner class WordLoaderTask(userList: ArrayList<User>) : AsyncTask<Void, ArrayList<User>, ArrayList<User>>() {

        private val height: Int = bitmap.height
        private val width: Int = bitmap.width
        private val dif: Double = height.toDouble() / width
        var db: DatabaseOpenHelper? = null
        private val uList: ArrayList<User> = userList

        override fun onPreExecute() {
            db = DatabaseOpenHelper(context!!)
            bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        }

        override fun doInBackground(vararg params: Void): ArrayList<User>? {
            val list: ArrayList<User> = db!!.readAllFollowers(myID!!, -1, myID!!)
            for (i in 0 until list.size)
                uList.add(list[i])

            //Log.e("kek", "mem ${listUser!!.size}")
            bitmapDrawable = BitmapDrawable(context!!.resources, bitmap)

            try { Thread.sleep(2000) }
            catch (e: InterruptedException) { e.printStackTrace() }

            return uList
        }

        override fun onPostExecute(result: ArrayList<User>?) {
            with(list) {
                //layoutManager = LinearLayoutManager(context)
                /*adapter = MyFollowersRecyclerViewAdapter(
                    result!!,
                    listener as OnListFragmentInteractionListener,
                    bitmapDrawable
                )*/
                adapter!!.notifyDataSetChanged()
            }
        }

    }*/
}