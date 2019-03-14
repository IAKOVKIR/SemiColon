package com.example.semicolon

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.util.Log
import androidx.navigation.findNavController
import com.example.semicolon.event.EventContent
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener {

    private lateinit var name: String
    private lateinit var user: String

    override fun onListFragmentInteraction(item: EventContent.Event?) {
        Log.i("Navigation", "Selected $item")
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
        name = b.getString("user") as String
        user = b.getString("user1") as String


        when (item.itemId) {
            R.id.navigation_home -> {
                //get username sent from the log in activity
                val args = Bundle()
                args.putString("param1", name)
                args.putString("param2", user)

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

        val intent = intent
        val b : Bundle = intent.extras as Bundle
        name = b.getString("user") as String
        user = b.getString("user1") as String

        val args = Bundle()
        args.putString("param1", name)
        args.putString("param2", user)

        findNavController(R.id.nav_host).navigate(R.id.main_dest, args)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}