package com.example.semicolon.semi_settings

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentSettingsBinding
import com.example.semicolon.login.LoginActivity
import com.example.semicolon.semi_settings.view_models.SettingsFragmentViewModel

/**
 * A fragment representing a list of Setting links.
 */
class SettingsFragment : Fragment() {

    //It is used to store and retrieve the data as key/value pairs on the device storage.
    private lateinit var n: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        n = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_settings, container, false)

        val settingsViewModel = ViewModelProvider(this).get(SettingsFragmentViewModel::class.java)

        val adapter = SettingsRecyclerViewAdapter(Setting.SETTING, settingsViewModel)
        binding.list.adapter = adapter

        // Add an Observer on the state variable for Navigating when one of the list items is clicked.
        settingsViewModel.settingId.observe(viewLifecycleOwner, Observer { selectedSettingId ->
            selectedSettingId?.let {

                when(selectedSettingId) {
                    1 -> this.findNavController().navigate(SettingsFragmentDirections
                        .actionSettingsFragmentToNotificationSettingsFragment())
                    2 -> this.findNavController().navigate(SettingsFragmentDirections
                        .actionSettingsFragmentToPasswordSettingsFragment(0))
                    3 -> this.findNavController().navigate(SettingsFragmentDirections
                        .actionSettingsFragmentToLanguageSettingsFragment())
                    4 -> this.findNavController().navigate(SettingsFragmentDirections
                        .actionSettingsFragmentToHelpSettingsFragment())
                    5 -> this.findNavController().navigate(SettingsFragmentDirections
                        .actionSettingsFragmentToAboutSettingsFragment())
                    else -> logOut()
                }

                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                settingsViewModel.onSettingNavigated()
            }
        })

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }

    /**
     * @function [logOut] removes all data from SharedPreferences and starts Login activity
     */
    private fun logOut() {
        val editor: SharedPreferences.Editor = n.edit()
        editor.clear()
        editor.apply()

        val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
        // set the new task and clear flags
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        requireActivity().finish()
    }
}