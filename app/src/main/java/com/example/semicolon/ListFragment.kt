package com.example.semicolon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.databinding.FragmentItemListBinding
import com.example.semicolon.models.EventContent.Event
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFragment.OnListFragmentInteractionListener] interface.
 */

class ListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    lateinit var db: DatabaseOpenHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentItemListBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_item_list, container, false)
        db = DatabaseOpenHelper(requireContext())
        val tabLayout: TabLayout = binding.tabs
        val list: RecyclerView = binding.list

        val listUser: ArrayList<Event> = ArrayList()
        val tabName: Array<String> = arrayOf("Trending", "Fun\uD83D\uDE02", "Innovation", "Marty & Michael", "Vitaly Uncensored")

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#D29B56AA"))

        for (i in 0..4) {
            tabLayout.addTab(tabLayout.newTab().setText(tabName[i]))
        }

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener)
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

        binding.editText4.setOnClickListener {view: View ->
            view.findNavController().navigate(ListFragmentDirections.actionListFragmentToUserSearchFragment())
        }

        return binding.root
    }

    private fun load() : ArrayList<Event> {
        return db.readAllEvents()
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
        fun onListFragmentInteraction(item: Event?)
    }
}