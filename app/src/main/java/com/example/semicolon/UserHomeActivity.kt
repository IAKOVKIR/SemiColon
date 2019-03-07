package com.example.semicolon

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.findNavController
import com.example.semicolon.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHomeActivity : AppCompatActivity(), ListFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.i("Navigation", "Selected $item")
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                findNavController(R.id.nav_host).navigate(R.id.main_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                findNavController(R.id.nav_host).navigate(R.id.list_dest)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
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
}
