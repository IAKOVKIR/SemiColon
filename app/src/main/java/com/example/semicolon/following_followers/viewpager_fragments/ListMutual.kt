package com.example.semicolon.following_followers.viewpager_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.R
import com.example.semicolon.databinding.ListMutualBinding
import com.example.semicolon.following_followers.viewpager_fragments.view_models.ListMutualViewModel
import com.example.semicolon.following_followers.viewpager_fragments.view_models.ListMutualViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.User
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListMutual.OnListFragmentInteractionListener] interface.
 */

class ListMutual : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var userId: Int? = null
    private var selectedUserId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt("user_id")
            selectedUserId = it.getInt("selected_user_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: ListMutualBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_mutual, container, false)
        val list: RecyclerView = binding.list
        val listUser: ArrayList<User> = ArrayList()

        val application = requireNotNull(this.activity).application
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        val viewModelFactory = ListMutualViewModelFactory(userId!!, selectedUserId!!, followerDataSource)
        val testViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ListMutualViewModel::class.java)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter =
                MutualRecyclerViewAdapter(
                    listUser,
                    listener as OnListFragmentInteractionListener,
                    application,
                    userId!!
                )
        }

        testViewModel.totalMutualFollowers.observe(viewLifecycleOwner, {
            it?.let {
                if (listUser.isEmpty()) {
                    listUser.addAll(it)
                    with(list) {
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }
        })

        return binding.root
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