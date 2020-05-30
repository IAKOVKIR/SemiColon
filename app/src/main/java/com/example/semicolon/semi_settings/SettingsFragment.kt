package com.example.semicolon.semi_settings

import android.os.Bundle
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentSettingsBinding
import com.example.semicolon.semi_settings.Setting.SettingItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SettingsFragment.OnListFragmentInteractionListener] interface.
 */
class SettingsFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_settings, container, false)

        // Set the adapter
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = SettingsRecyclerViewAdapter(Setting.SETTING, listener)
        }

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

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

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SettingItem?)
    }
}