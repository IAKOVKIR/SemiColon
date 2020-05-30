package com.example.semicolon.semi_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentAboutSettingsBinding
//import com.example.semicolon.following_followers.FollowingFollowersFragmentArgs

/**
 * A simple [Fragment] subclass.
 */
class AboutSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAboutSettingsBinding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_about_settings, container, false)
        //val args = FollowingFollowersFragmentArgs.fromBundle(requireArguments())
        //val myID: Int = args.myId //myID

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }
}