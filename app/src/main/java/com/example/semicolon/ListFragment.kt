package com.example.semicolon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var listUser: ArrayList<Event> = ArrayList()
    lateinit var list: RecyclerView
    lateinit var db: DatabaseOpenHelper
    private var job: Job = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_item_list, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#D29B56AA"))

        repeat(5) {
            tabLayout.addTab(tabLayout.newTab())
        }

        tabLayout.getTabAt(0)!!.text = "Trending"
        tabLayout.getTabAt(1)!!.text = "Fun\uD83D\uDE02"
        tabLayout.getTabAt(2)!!.text = "Innovation"
        tabLayout.getTabAt(3)!!.text = "Abbas & Phi Thien"
        tabLayout.getTabAt(4)!!.text = "Vitaly Uncensored"


        list = view.findViewById(R.id.list)
        db = DatabaseOpenHelper(requireContext())

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener)
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

        val searchBar: EditText = view.findViewById(R.id.editText4)
        //searchBar.isEnabled = false
        searchBar.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack("list_to_user_search")
                .replace(R.id.nav_host, UserSearchFragment(), "list_to_user_search")
                .commit()
        }

        return view
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
        job.cancel()
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