package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semicolon.models.EventContent
import com.example.semicolon.models.Friend
import com.example.semicolon.models.User
import com.example.semicolon.semi_registration.FirstRegistrationActivity
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.support_features.Time


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
            db.insertUser(
            User(
                "mr_query", "0490506763", "12345678", "Chandra MrQuery", "I'll still fuck your bitch", "MrStealYourQuery@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "ice_wallow", "0490000000", "12345678", "Abbas Ice-Wallow", "I'm better than my father", "BetterThanFather@gmail.com",
                2.8F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "karama", "0490000001", "12345678", "Carl Karama", "Mr.President", "CallMePresident@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "merin", "0490000002", "12345678", "Matt Merin", "Always on the beat", "MattMerinOnTheBeat@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "ay_sir", "0490000003", "12345678", "John Carlos", "Aye, sir", "JohnCarlos@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "beverly", "0490000004", "12345678", "Patrick Beverly", "PB", "beverly@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "dwade", "0490000005", "12345678", "Dwyane Wade", "flash", "wade@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "phelps", "0490000006", "12345678", "Michael Phelps", "splash", "phelps@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "putin", "0490000007", "12345678", "Vladimir Putin", "King of the Vodkaland", "putin@kgb.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "harden13", "0490000008", "12345678", "James Harden", "The Beard", "jharden@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )
        db.insertUser(
            User(
                "king_james", "0490000009", "12345678", "LeBron James", "King James", "KingJames@gmail.com",
                5.0F, time.toString(), time.toString()
            )
        )

        //test entries for FRIEND table
        db.insertRequest(
            Friend(
                1,
                1,
                2,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                2,
                1,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                3,
                2,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                4,
                3,
                1,
                time.getDate(),
                time.getTime(),
                1
            )
        )
        db.insertRequest(
            Friend(
                5,
                1,
                4,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                6,
                2,
                4,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                7,
                3,
                4,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                8,
                4,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                9,
                5,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                10,
                6,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                11,
                7,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                12,
                8,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                13,
                9,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                14,
                10,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )
        db.insertRequest(
            Friend(
                15,
                11,
                3,
                time.getDate(),
                time.getTime(),
                0
            )
        )


        //test entries for EVENT table
        db.insertEvent(
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
            time.getDate(), time.getDate(), time.getTime(), time.getTime()))


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

        if (user.userId != -1) {
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

        if (list.userId != -1 && username != "")
            showHome()
    }

    /**
     * @function rememberMe() saves username, password and other values in SharedPreferences
     */

    //rememberMe() function saves id, phone number and password in SharedPreferences
    private fun rememberMe(user: User) {
        getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            .edit()
            .putInt("id", user.userId)
            .putString("username", user.username)
            .putString("password", user.password)
            .apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

}