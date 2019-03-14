package com.example.semicolon

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private var param1 : String? = null
    private var param2 : String? = null
    private var settingsButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val fullName = "$param1 $param2"
        val nameText = view.findViewById<TextView>(R.id.name)

        nameText.text = fullName

        settingsButton = view.findViewById(R.id.settings_button)
        settingsButton!!.setOnClickListener {
            val fragment: Fragment = SettingsFragment()
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            transaction.addToBackStack(null)
            // Commit the transaction
            transaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }

}