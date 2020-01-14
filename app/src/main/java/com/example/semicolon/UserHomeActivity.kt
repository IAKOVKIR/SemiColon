package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.example.semicolon.semi_settings.Setting
import com.example.semicolon.event.EventContent
import com.example.semicolon.semi_settings.SettingFragment
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener,
    SettingFragment.OnListFragmentInteractionListener {

    private var myID: Int? = null
    private lateinit var n: SharedPreferences

    //listener for settings list
    override fun onListFragmentInteraction(item: Setting.SettingItem?) {

        myID = n.getInt("id", 1)

        val args = Bundle()
        args.putString("param1", item!!.name)
        args.putString("param2", item.pos)
        args.putInt("MyID", myID!!)

        if (item.pos == "6")
            logOut()
        else
            findNavController(R.id.nav_host).navigate(R.id.setting_params_dest, args)
    }

    //listener for events list
    override fun onListFragmentInteraction(item: EventContent.Event?) {
        val args = Bundle()
        args.putString("param1", "Selected")
        args.putString("param2", item.toString())
        args.putString("param3", item!!.details)
        args.putInt("param4", item.image)

        findNavController(R.id.nav_host).navigate(R.id.params_dest, args)
    }

    //listener for bottom navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                //opens main fragment
                findNavController(R.id.nav_host).navigate(R.id.main_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //opens event list fragment (ListFragment.kt)
                findNavController(R.id.nav_host).navigate(R.id.list_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val args = Bundle()
                args.putString("MyFirstName", n.getString("FName", ""))
                args.putString("MyLastName", n.getString("LName", ""))

                //opens notifications fragment and sends arguments
                findNavController(R.id.nav_host).navigate(R.id.action_global_params_dest, args)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        n = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        //add navigation listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     * @function [logOut] removes all data from SharedPreferences and starts Login activity
     */
    private fun logOut() {
        val editor: SharedPreferences.Editor = n.edit()
        editor.clear()
        editor.apply()

        val loginIntent = Intent(this, Login::class.java)
        startActivity(loginIntent)
        finish()
    }
}