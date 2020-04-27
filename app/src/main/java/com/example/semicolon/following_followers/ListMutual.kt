package com.example.semicolon.following_followers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

// the fragment initialization parameters
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"
private const val EXCEPTION_ID = "exception_id"

class ListMutual : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var myID: Int? = null
    private var userID: Int? = null
    private var exceptionID: Int? = null
    private var listUser: ArrayList<User> = ArrayList()
    lateinit var list: RecyclerView
    lateinit var db: DatabaseOpenHelper
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID)
            userID = it.getInt(USER_ID)
            exceptionID = it.getInt(EXCEPTION_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.mutual_followers_list, container, false)
        list = view.findViewById(R.id.list)
        db = DatabaseOpenHelper(requireContext())

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MutualRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener, myID!!)
            setHasFixedSize(true)
        }

        job = CoroutineScope(Dispatchers.Default).launch {

            if (listUser.isEmpty())
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
        return db.readAllMutualFollowers(myID!!, userID!!)
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