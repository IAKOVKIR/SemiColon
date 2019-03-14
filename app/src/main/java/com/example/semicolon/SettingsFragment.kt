package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.AdapterView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    var simpleList: ListView? = null
    var countryList = arrayOf("Notifications", "Change Password", "Language", "Help", "About", "Log Out")
    private var log = Login()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        simpleList = view.findViewById(R.id.settings_list) as ListView
        val arrayAdapter = ArrayAdapter<String>(view.context, R.layout.fragment_chosen_settings, R.id.text_text, countryList)//ArrayAdapter<String>(view.context, R.layout.fragment_settings, R.id.settings_list, countryList)
        simpleList!!.adapter = arrayAdapter

        simpleList!!.onItemClickListener = AdapterView.OnItemClickListener { /*parent*/_, /*view*/_, position, /*id*/_ ->
            //The position where the list item is clicked is obtained from the
            //the parameter position of the android listview

            //Get the String value of the item where the user clicked
            val itemValue = simpleList!!.getItemAtPosition(position) as String

            val args = Bundle()
            args.putString("param1", "$position")
            args.putString("param2", itemValue)

            if (position != 5) {

                val fragment: Fragment = ChosenSettings()
                fragment.arguments = args
                val manager = fragmentManager
                val transaction = manager!!.beginTransaction()
                transaction.add(R.id.nav_host, fragment)
                transaction.addToBackStack(null)
                // Commit the transaction
                transaction.commit()

            } else {

                val sharedPrefs = activity!!.getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.clear()
                editor.apply()

                val loginIntent = Intent(view.context, Login::class.java)
                startActivity(loginIntent)
                activity!!.finish()

            }
        }

        return view
    }

}