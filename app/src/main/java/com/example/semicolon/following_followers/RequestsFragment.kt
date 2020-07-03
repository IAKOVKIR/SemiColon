package com.example.semicolon.following_followers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentRequestsBinding
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

class RequestsFragment : Fragment() {
    private var listener: OnListFragmentInteractionListener? = null
    private var userID: Int? = null
    lateinit var db: DatabaseOpenHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentRequestsBinding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_requests, container, false)
        val list: RecyclerView = binding.list
        db = DatabaseOpenHelper(requireContext())
        val listUser: ArrayList<User> = ArrayList()

        val args = RequestsFragmentArgs.fromBundle(requireArguments())
        val myID: Int = args.myId //myID
        userID = args.userId //myID

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = RequestsRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener, myID)
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

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }

    private fun load() : ArrayList<User> {
        return db.readAllFollowers(userID!!, 0)
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