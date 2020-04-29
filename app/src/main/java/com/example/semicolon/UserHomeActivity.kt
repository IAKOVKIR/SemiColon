package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.semicolon.models.EventContent
import com.example.semicolon.models.User
import com.example.semicolon.following_followers.ListFollowers
import com.example.semicolon.following_followers.ListFollowing
import com.example.semicolon.following_followers.ListMutual
import com.example.semicolon.following_followers.RequestsFragment
import com.example.semicolon.semi_settings.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_user_home.*

// the fragment initialization parameters, e.g MY_ID, USER_ID and EXCEPTION_ID
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"
private const val EXCEPTION_ID = "exception_id"
private const val EVENT_ID = "event_id"

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener,
    ListFollowers.OnListFragmentInteractionListener, ListFollowing.OnListFragmentInteractionListener,
    UserSearchFragment.OnListFragmentInteractionListener, RequestsFragment.OnListFragmentInteractionListener,
        ListMutual.OnListFragmentInteractionListener, SettingFragment.OnListFragmentInteractionListener,
        ListSearchFragment.OnListFragmentInteractionListener
{

    private var myID: Int? = null
    private lateinit var n: SharedPreferences

    override fun onListFragmentInteraction(item: User?) {
        myID = n.getInt("id", 1)

        if (item!!.userId != myID) {
            val args = Bundle()
            args.putInt(MY_ID, myID!!)
            args.putInt(USER_ID, item.userId)
            args.putInt(EXCEPTION_ID, myID!!)

            val t = FriendFragment()
            t.arguments = args
            startFragment(t, "to_friend")
        } else
            startFragment(MainFragment(), "open_main")
    }

    //listener for events list
    override fun onListFragmentInteraction(item: EventContent.Event?) {
        myID = n.getInt("id", 1)

        val args = Bundle()
        args.putInt(MY_ID, myID!!)
        args.putInt(EVENT_ID, item!!.eventId)

        val t = EventFragment()
        t.arguments = args
        startFragment(t, "open_event_page")
    }

    override fun onListFragmentInteraction(item: Setting.SettingItem?) {

        when (item!!.pos) {
            1 -> startFragmentArgs(NotificationSettingsFragment(), "to_notifications_settings")
            2 -> startFragmentArgs(PasswordSettingsFragment(), "to_password_settings")
            3 -> startFragmentArgs(LanguageSettingsFragment(), "to_language_settings")
            4 -> startFragmentArgs(HelpSettingsFragment(), "to_help_settings")
            5 -> startFragmentArgs(AboutSettingsFragment(), "to_about_settings")
            else -> logOut()
        }

    }

    //listener for bottom navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                //opens main fragment
                val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host)
                if (currentFragment !is MainFragment)
                    startFragment(MainFragment(), "open_main")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //opens event list fragment (ListFragment.kt)
                val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host)
                if (currentFragment !is ListFragment)
                    startFragment(ListFragment(), "open_list")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host)
                if (currentFragment !is ListSearchFragment)
                    startFragmentArgs(ListSearchFragment(), "open_list_search")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun startFragmentArgs(fragment: Fragment, key: String) {
        myID = n.getInt("id", 1)

        val args = Bundle()
        args.putInt(MY_ID, myID!!)

        fragment.arguments = args
        startFragment(fragment, key)
    }

    private fun startFragment(fragment: Fragment, key: String) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(key)
            .replace(R.id.nav_host, fragment, key)
            .commit()
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
        // set the new task and clear flags
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        finish()
    }
}