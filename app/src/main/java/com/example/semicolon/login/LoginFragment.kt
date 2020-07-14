package com.example.semicolon.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.DataRecovery
import com.example.semicolon.LoginRecyclerView
import com.example.semicolon.R
import com.example.semicolon.UserHomeActivity
import com.example.semicolon.databinding.FragmentLoginBinding
import com.example.semicolon.semi_registration.FirstRegistrationActivity
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var userDataSource: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application
        userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginBinding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)

        val viewModelFactory = LoginViewModelFactory(userDataSource)
        val loginViewModel =
            ViewModelProvider(
                ViewModelStore(), viewModelFactory).get(LoginViewModel::class.java)

        //test entries for EVENT table
        /*db.insertEvent(
            EventContent.Event(1, "Event1", 30, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))
        db.insertEvent(
            EventContent.Event(2, "Event2", 10, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))
        db.insertEvent(
            EventContent.Event(3, "Event3", 20, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))
        db.insertEvent(
            EventContent.Event(4, "Event4", 40, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))
        db.insertEvent(
            EventContent.Event(5, "Event5", 80, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))
        db.insertEvent(
            EventContent.Event(6, "Event6", 320, "Melbourne",
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))*/


        val fEnter = loginBinding.userName
        val sEnter = loginBinding.password
        fEnter.setHintTextColor(Color.WHITE)
        sEnter.setHintTextColor(Color.WHITE)

        //"Sign In" listener
        loginBinding.logBut.setOnClickListener {
            loginViewModel.checkUser(fEnter.text.toString(), sEnter.text.toString())
        }

        loginViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                //saves user's id, phone number and password in SharedPreferences
                rememberMe(user)
            } else {
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show()
                val shake: Animation = AnimationUtils.loadAnimation(context,
                    R.anim.editext_shaker
                )
                fEnter.startAnimation(shake)
                sEnter.startAnimation(shake)
            }
        })

        //Buttons
        //forgot info button
        loginBinding.forgotBut.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

            //"Recovery" listener
            setOnClickListener {
                val intent = Intent(requireActivity(), DataRecovery::class.java)
                startActivity(intent)
            }
        }

        //create an account button
        loginBinding.createAnAccountBut.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

            //"Sign Up" listener
            setOnClickListener {
                val intent = Intent(requireActivity(), FirstRegistrationActivity::class.java)
                startActivity(intent)
            }
        }

        // set up the RecyclerView
        val recyclerView: RecyclerView = loginBinding.rvUsers
        val horizontalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        val adapter = LoginRecyclerView(10)
        recyclerView.adapter = adapter

        return loginBinding.root
    }

    /**
     * @function showHome() starts UserHomeActivity
     */
    private fun showHome() {
        val intent = Intent(this.activity, UserHomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    /**
     * @function getUser() checks username and password.
     * If there is an account with same username and password, then UserHomeActivity activity will be opened directly
     */

    private fun getUser() {
        val pref: SharedPreferences = requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val username: String = pref.getString("username", "")!!.trim()
        val password: String = pref.getString("password", "")!!.trim()
        Log.e("res", username)
        //loginViewModel.checkUser(username, password)
        CoroutineScope(Dispatchers.Default).launch {

            var user: User? = null

            withContext(Dispatchers.IO) {
                if (username.trim().length > 1 && password.trim().length > 7) {
                    user = userDataSource.getUserByUsernameAndPassword(username, password)
                }
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                if (user != null) {
                    showHome()
                }
            }

        }
    }

    /**
     * @function rememberMe() saves username, password and other values in SharedPreferences
     */

    //rememberMe() function saves id, phone number and password in SharedPreferences
    private fun rememberMe(user: User) {
        requireActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                .edit()
                .putInt("id", user.userId)
                .putString("username", user.username)
                .putString("password", user.password)
                .apply()

        //show UserHomeActivity activity
        showHome()
    }
}