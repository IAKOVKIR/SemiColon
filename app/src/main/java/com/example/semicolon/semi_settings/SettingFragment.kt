package com.example.semicolon.semi_settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
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

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings_list, container, false)
        val list: RecyclerView = view.findViewById(R.id.list)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = SettingsRecyclerViewAdapter(Setting.SETTING, listener)
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

                val fragment: Fragment = MainFragment()
                val manager: FragmentManager? = fragmentManager
                val transaction: FragmentTransaction = manager!!.beginTransaction()
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
}