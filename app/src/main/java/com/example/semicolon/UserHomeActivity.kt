package com.example.semicolon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentActivity
import android.util.Log
import androidx.navigation.findNavController
import com.example.semicolon.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : FragmentActivity(), ListFragment.OnListFragmentInteractionListener {

    private lateinit var name: String
    private lateinit var user: String
    private var log = Login()

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.i("Navigation", "Selected $item")
        val args = Bundle()
        args.putString("param1", "Selected")
        args.putString("param2", item.toString())
        findNavController(R.id.nav_host).navigate(R.id.action_to_params, args)
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
                findNavController(R.id.nav_host).navigate(R.id.action_global_params, args)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_logout -> {
                    val sharedPrefs = getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.clear()
                    editor.apply()
                    user = ""

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
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
