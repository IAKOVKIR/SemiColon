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

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener, SettingFragment.OnListFragmentInteractionListener,
                                       FriendsFragment.OnListFragmentInteractionListener {

    //data from login activity
    var list: ArrayList<String>? = null
    private var log = Login()
    var text : TextView? = null

    override fun onListFragmentInteraction(item: User?) {
        Log.i("Navigation", "Selected Friend $item")
        val args = Bundle()
        args.putString("param1", item!!.firstName)
        args.putString("param2", item.lastName)
        args.putStringArrayList("param3", list)

        findNavController(R.id.nav_host).navigate(R.id.friend_params_dest, args)
    }

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

    override fun onListFragmentInteraction(item: EventContent.Event?) {
        Log.i("Navigation", "Selected Event $item")
        val args = Bundle()
        args.putString("param1", "Selected")
        args.putString("param2", item.toString())
        args.putString("param3", item!!.details)
        args.putInt("param4", item.image)

        findNavController(R.id.nav_host).navigate(R.id.params_dest, args)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val intent = intent
        val b : Bundle = intent.extras as Bundle
        list = b.getStringArrayList("user")

        when (item.itemId) {
            R.id.navigation_home -> {
                //get username sent from the log in activity
                val args = Bundle()
                args.putStringArrayList("user", list)

                findNavController(R.id.nav_host).navigate(R.id.main_dest, args)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                findNavController(R.id.nav_host).navigate(R.id.list_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val args = Bundle()
                args.putString("param1", "Chandra")
                args.putString("param2", "Lil Query")
                findNavController(R.id.nav_host).navigate(R.id.action_global_params_dest, args)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

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