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
private const val ARG_PARAM1 = "user"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    private var param1 : ArrayList<String>? = null
    private var settingsButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getStringArrayList(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val fullName = "${param1?.get(1)} ${param1?.get(2)}"

        val nameText = view.findViewById<TextView>(R.id.name)
        val friendsLink = arrayOf<TextView>(view.findViewById(R.id.friends_word), view.findViewById(R.id.friends_number))

        nameText.text = fullName

        friendsLink[0].setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        friendsLink[1].setOnClickListener {
            val fragment: Fragment = FriendsFragment()
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            // Commit the transaction
            transaction.commit()
        }

        settingsButton = view.findViewById(R.id.settings_button)
        settingsButton!!.setOnClickListener {
            val fragment: Fragment = SettingFragment()
            val manager = fragmentManager
            val transaction = manager!!.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            //transaction.addToBackStack(null)
            // Commit the transaction
            transaction.commit()
        }

        // Inflate the layout for this fragment
        return view
    }

}