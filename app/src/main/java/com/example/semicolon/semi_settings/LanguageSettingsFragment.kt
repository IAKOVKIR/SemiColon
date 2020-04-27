package com.example.semicolon.semi_settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager

import com.example.semicolon.R

// the fragment initialization parameter, e.g MY_ID
private const val MY_ID = "my_id"

/**
 * A simple [Fragment] subclass.
 */
class LanguageSettingsFragment : Fragment() {

    private var myID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID) //myID
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_language_settings, container, false)
        val backButton: TextView = view.findViewById(R.id.back_button)

        backButton.setOnClickListener {
            val fm: FragmentManager = parentFragmentManager
            fm.popBackStack("to_language_settings", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        return view
    }
}