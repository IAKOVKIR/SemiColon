package com.example.semicolon

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat

import com.example.semicolon.event.EventContent
import com.example.semicolon.event.EventContent.Event
import com.google.android.material.tabs.TabLayout

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ListFragment.OnListFragmentInteractionListener] interface.
 */

class ListFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_item_list, container, false)
        val list: RecyclerView = view.findViewById(R.id.list)

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

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyItemRecyclerViewAdapter(EventContent.EVENTS, listener)
        }

        val searchBar: EditText = view.findViewById(R.id.editText4)
        searchBar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val line: String = searchBar.text.toString()
                list.adapter = MyItemRecyclerViewAdapter(EventContent.getList(line), listener)

            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
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