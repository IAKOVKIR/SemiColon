package com.example.semicolon.semi_settings

import android.os.Bundle
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.semicolon.R
import com.example.semicolon.semi_settings.Setting.SettingItem

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
        val backButton: TextView = view.findViewById(R.id.back_button)

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = SettingsRecyclerViewAdapter(Setting.SETTING, listener)
        }

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_settings", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SettingItem?)
    }
}