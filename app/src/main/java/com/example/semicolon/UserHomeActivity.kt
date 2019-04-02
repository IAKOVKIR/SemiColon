package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.semicolon.setting.Setting
import com.example.semicolon.event.EventContent
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener,
    SettingFragment.OnListFragmentInteractionListener, FriendsFragment.OnListFragmentInteractionListener {

    /*
    variable list contains ArrayList<String> array with user's data from shared preferences
    log is an object from Login Class
     */
    var list: ArrayList<String>? = null
    private var log = Login()
    var text : TextView? = null

    //listener for users list
    override fun onListFragmentInteraction(item: User?) {
        Log.i("Navigation", "Selected Friend $item")
        val args = Bundle()
        args.putString("param1", item!!.firstName)
        args.putString("param2", item.lastName)
        args.putStringArrayList("param3", list)

        findNavController(R.id.nav_host).navigate(R.id.friend_params_dest, args)
    }

    //listener for settings list
    override fun onListFragmentInteraction(item: Setting.SettingItem?) {
        Log.i("Navigation", "Selected Setting $item")
        val args = Bundle()
        args.putString("param1", item!!.name)
        args.putString("param2", item.pos)
        args.putStringArrayList("param3", list)

        if (item.pos == "6")
            logOut()
        else
            findNavController(R.id.nav_host).navigate(R.id.setting_params_dest, args)
    }

    //listener for events list
    override fun onListFragmentInteraction(item: EventContent.Event?) {
        Log.i("Navigation", "Selected Event $item")
        val args = Bundle()
        args.putString("param1", "Selected")
        args.putString("param2", item.toString())
        args.putString("param3", item!!.details)
        args.putInt("param4", item.image)

        findNavController(R.id.nav_host).navigate(R.id.params_dest, args)
    }

    //listener for bottom navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val n = getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
        list = arrayListOf(n.getString(log.prefVar[0], "") as String, n.getString(log.prefVar[1], "") as String,
            n.getString(log.prefVar[2], "") as String, n.getString(log.prefVar[3], "") as String,
            n.getString(log.prefVar[4], "") as String, n.getString(log.prefVar[5], "") as String,
            n.getString(log.prefVar[6], "") as String, n.getString(log.prefVar[7], "") as String)

        when (item.itemId) {
            R.id.navigation_home -> {
                //opens main fragment
                findNavController(R.id.nav_host).navigate(R.id.main_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //opens event list fragment
                findNavController(R.id.nav_host).navigate(R.id.list_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val args = Bundle()
                args.putString("param1", n.getString(log.prefVar[1], ""))
                args.putString("param2", n.getString(log.prefVar[2], ""))

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

        //add navigation listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /**
     * @function [logOut] removes all data from SharedPreferences and starts Login activity
     */
    private fun logOut() {
        val sharedPrefs = getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()

        val loginIntent = Intent(this, Login::class.java)
        startActivity(loginIntent)
        finish()
    }
}