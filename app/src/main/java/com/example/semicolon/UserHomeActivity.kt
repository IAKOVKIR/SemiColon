package com.example.semicolon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.example.semicolon.models.EventContent
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowers
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowing
import com.example.semicolon.following_followers.viewpager_fragments.ListMutual
import com.example.semicolon.sqlite_database.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_user_home.*

// the fragment initialization parameters
private const val SELECTED_USER_ID = "selected_user_id"
private const val USER_ID = "user_id"
private const val EVENT_ID = "event_id"

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener,
    ListFollowers.OnListFragmentInteractionListener, ListFollowing.OnListFragmentInteractionListener,
    UserSearchFragment.OnListFragmentInteractionListener, ListMutual.OnListFragmentInteractionListener,
    ListSearchFragment.OnListFragmentInteractionListener {

    private var userId: Int? = null
    private lateinit var n: SharedPreferences

    override fun onListFragmentInteraction(item: User?) {
        val args = Bundle()
        args.putInt(USER_ID, userId!!)
        args.putInt(SELECTED_USER_ID, item!!.userId)

        findNavController(R.id.nav_host).navigate(R.id.friendFragment, args)
    }

    //listener for events list
    override fun onListFragmentInteraction(item: EventContent.Event?) {
        val args = Bundle()
        args.putInt(USER_ID, userId!!)
        args.putInt(EVENT_ID, item!!.eventId)

        val t = EventFragment()
        t.arguments = args
        startFragment(t, "open_event_page")
    }

    //listener for bottom navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                //opens main fragment
                if (findNavController(R.id.nav_host).currentDestination!!.id != R.id.mainFragment) {
                    findNavController(R.id.nav_host).navigate(R.id.mainFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                //opens event list fragment (ListFragment.kt)
                if (findNavController(R.id.nav_host).currentDestination!!.id != R.id.listFragment) {
                    findNavController(R.id.nav_host).navigate(R.id.listFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host)
                if (currentFragment !is ListSearchFragment)
                    startFragmentArgs(ListSearchFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun startFragmentArgs(fragment: Fragment) {
        userId = n.getInt("id", 1)

        val args = Bundle()
        args.putInt(USER_ID, userId!!)

        fragment.arguments = args
        startFragment(fragment, "open_list_search")
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
        userId = n.getInt("id", 1)
        //add navigation listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}