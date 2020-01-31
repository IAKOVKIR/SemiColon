package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.semi_registration.FirstRegistrationActivity
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.time.Time


class Login : Activity() {

    //EditTexts
    private lateinit var fEnter : EditText //username
    private lateinit var sEnter : EditText //password
    private var adapter: LoginRecyclerView? = null

    private lateinit var db : DatabaseOpenHelper

    public override fun onStart() {
        super.onStart()
        //reads username and password from SharedPreferences
        getUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val time = Time()

        db = DatabaseOpenHelper(this)

        //test entries for USER table
        db.insertUser(User("mr_query", "Chandra", "MrQuery", "0490506763", "12345678",
            5.0F, "MrStealYourQuery@gmail.com"))
        db.insertUser(User("ice_wallow", "Abbas", "Ice Wallow", "0490000000", "12345678",
             3.2F, "BetterThanFather@gmail.com"))
        db.insertUser(User("karama","Carl", "Obama", "0490000001", "12345678",
             5.0F, "CallMePresident@gmail.com"))
        db.insertUser(User("merin","Matt", "Merin", "0490000002", "12345678",
             5.0F, "MattMerinOnTheBeat@gmail.com"))
        db.insertUser(User("ay_sir", "John", "Carlos", "0490000003", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("beverly", "Patrick", "Beverly", "0490000004", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("dwade", "Dwyane", "Wade", "0490000005", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("phelps", "Michael", "Phelps", "0490000006", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("putin", "Vladimir", "Putin", "0490000007", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("harden13", "James", "Harden", "0490000008", "12345678",
             5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User("king_james","Lebron", "James", "0490000009", "12345678",
             5.0F, "MrStealYourQuery@gmail.com"))

        //test entries for FRIEND table
        db.insertRequest(Friend(1, 1, 2, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(2, 1, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(3, 2, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(4, 3, 1, time.getDate(), time.getTime(), 1))
        db.insertRequest(Friend(5, 1, 4, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(6, 2, 4, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(7, 3, 4, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(8, 4, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(9, 5, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(10, 6, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(11, 7, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(12, 8, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(13, 9, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(14, 10, 3, time.getDate(), time.getTime(), 0))
        db.insertRequest(Friend(15, 11, 3, time.getDate(), time.getTime(), 0))

        fEnter = findViewById(R.id.user_name)
        sEnter = findViewById(R.id.password)
        fEnter.setHintTextColor(Color.WHITE)
        sEnter.setHintTextColor(Color.WHITE)

        //Buttons
        val logBut: Button = findViewById(R.id.logBut)
        val forgotBut: TextView = findViewById(R.id.forgotBut)
        val createBut: TextView = findViewById(R.id.create_an_account_but)

        //"Sign In" listener
        logBut.setOnClickListener {
            doLogin()
        }

        forgotBut.paintFlags = createBut.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        createBut.paintFlags = createBut.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //"Recovery" listener
        forgotBut.setOnClickListener {
            val intent = Intent(this, DataRecovery::class.java)
            startActivity(intent)
        }

        //"Sign Up" listener
        createBut.setOnClickListener {
            val intent = Intent(this, FirstRegistrationActivity::class.java)
            startActivity(intent)
        }

        // set up the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.rvAnimals)
        val horizontalLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        adapter = LoginRecyclerView(10)
        //adapter.setClickListener(this)
        recyclerView.adapter = adapter

    }

    /**
     * @function doLogin() retrieves data from two EditText's (txtPhone - phone number, txtPassword - password)
     * If there is a user with the same phone number and password as txtPhone and txtPassword, then HomeUserActivity will be started
     * Otherwise 'Toast' message with and error will be displayed
     * @function rememberMe(user: User) saves user's phone number and password in SharedPreferences
     */
    private fun doLogin() {
        val txtUsername: String = fEnter.text.toString()
        val txtPassword: String = sEnter.text.toString()
        val user: User = db.findUserByUsernameAndPassword(txtUsername, txtPassword)

        if (user.id != -1) {
            //saves user's id, phone number and password in SharedPreferences
            rememberMe(user)
            //show UserHomeActivity activity
            showHome()
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
            val shake: Animation = AnimationUtils.loadAnimation(this@Login, R.anim.editext_shaker)
            fEnter.startAnimation(shake)
            sEnter.startAnimation(shake )
        }
    }

    /**
     * @function showHome() starts UserHomeActivity
     */

    private fun showHome() {
        val intent = Intent(this, UserHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @function getUser() checks username and password.
     * If there is an account with same username and password, then UserHomeActivity activity will be opened directly
     */

    private fun getUser() {
        val pref: SharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val username: String = pref.getString("username", "") as String
        val password: String = pref.getString("password", "") as String
        val list: User = db.findUserByUsernameAndPassword(username, password)

        if (list.id != -1 && username != "")
            showHome()
    }

    /**
     * @function rememberMe() saves username, password and other values in SharedPreferences
     */

    //rememberMe() function saves id, phone number and password in SharedPreferences
    private fun rememberMe(user: User) {
        getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            .edit()
            .putInt("id", user.id)
            .putString("username", user.username)
            .putString("password", user.password)
            .apply()
    }

}