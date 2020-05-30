package com.example.semicolon.following_followers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.R
import com.example.semicolon.databinding.ListFollowingBinding
import com.example.semicolon.models.User
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFollowing.OnListFragmentInteractionListener] interface.
 */

class ListFollowing : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var myID: Int? = null
    private var userID: Int? = null
    lateinit var db: DatabaseOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt("my_id")
            userID = it.getInt("user_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ListFollowingBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_following, container, false)
        val list: RecyclerView = binding.list
        db = DatabaseOpenHelper(requireContext())
        val listUser: ArrayList<User> = ArrayList()

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = FollowingRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener,
                myID!!)
            setHasFixedSize(true)
        }

        CoroutineScope(Dispatchers.Default).launch {

            if (listUser.isEmpty())
                listUser.addAll(withContext(Dispatchers.Default) { load() })

            CoroutineScope(Dispatchers.Main).launch {
                with(list) {
                    adapter!!.notifyDataSetChanged()
                }
            }

        }

        return binding.root
    }

    private fun load() : ArrayList<User> {
        return db.readAllFollowing(userID!!)
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
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }
}