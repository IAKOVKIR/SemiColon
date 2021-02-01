package com.example.semicolon.semi_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentHelpSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class HelpSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentHelpSettingsBinding = DataBindingUtil.inflate(
                            inflater, R.layout.fragment_help_settings, container, false)

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}