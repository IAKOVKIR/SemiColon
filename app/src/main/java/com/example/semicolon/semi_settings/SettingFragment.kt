package com.example.semicolon.semi_settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.semicolon.semi_settings.Setting.SettingItem
import android.view.KeyEvent
import com.example.semicolon.MainFragment
import com.example.semicolon.R


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SettingFragment.OnListFragmentInteractionListener] interface.
 */
class SettingFragment : Fragment() {

    private var columnCount = 1
    private var param1 : ArrayList<String>? = null
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            param1 = it.getStringArrayList("user")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_list, container, false)
        val list = view.findViewById<RecyclerView>(R.id.list)

        // Set the adapter
        if (list is RecyclerView) {
            with(list) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = SettingsRecyclerViewAdapter(Setting.SETTING, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener)
            listener = context
        else
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
    }

    override fun onResume() {
        super.onResume()
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                val args = Bundle()
                args.putStringArrayList("user", param1)

                val fragment: Fragment = MainFragment()
                fragment.arguments = args
                val manager = fragmentManager
                val transaction = manager!!.beginTransaction()
                transaction.replace(R.id.nav_host, fragment)
                // Commit the transaction
                transaction.commit()

                true
            } else
                false
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SettingItem?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}