package com.example.semicolon.following_followers

//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
//import android.util.Log
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

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
    //private lateinit var bitmap: Bitmap
    //private lateinit var bitmapDrawable: BitmapDrawable
    private var listUser: ArrayList<User> = ArrayList()
    lateinit var list: RecyclerView
    lateinit var db: DatabaseOpenHelper
    private var job: Job = Job()

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
        db = DatabaseOpenHelper(context!!)

        /*Log.i("check", "$exceptionID")
        bitmap = BitmapFactory.decodeResource(view.resources, R.drawable.smithers)
        val height: Int = bitmap.height
        val width: Int = bitmap.width
        val dif: Double = height.toDouble() / width
        bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
        bitmapDrawable = BitmapDrawable(context!!.resources, bitmap)*/

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyFollowersRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener/*,
                bitmapDrawable*/)
            setHasFixedSize(true)
        }

        job = CoroutineScope(Dispatchers.Default).launch {

            listUser.addAll(withContext(Dispatchers.Default) { load() })

            CoroutineScope(Dispatchers.Main).launch {
                with(list) {
                    adapter!!.notifyDataSetChanged()
                }
            }

        }

        return view
    }

    private fun load() : ArrayList<User> {
        return db.readAllFollowers(userID!!, -1, myID!!)
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
        job.cancel()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

}