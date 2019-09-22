package com.example.semicolon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.semi_registration.Registration
import com.example.semicolon.time.Time

class Login : Activity() {

    //variables (SharedPreferences)
    var prefName = "myPreferences"
    var prefVar: Array<String> = arrayOf("id", "firstName", "lastName", "phone", "password", "city", "rating", "email")

    //EditTexts
    private lateinit var fEnter : EditText //username
    private lateinit var sEnter : EditText //password

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

        /*db.insertUser(User(1,"Chandra", "MrQuery", "0490506763", "12345678",
            "Melbourne", 1, 5.0F, "MrStealYourQuery@gmail.com"))
        db.insertUser(User(2,"Abbas", "Ice Wallow", "0490000000", "12345678",
            "Somewhere", 1, 3.2F, "BetterThanFather@gmail.com"))
        db.insertUser(User(3,"Carl", "Obama", "0490000001", "12345678",
            "Melbourne", 1, 5.0F, "CallMePresident@gmail.com"))
        db.insertUser(User(4,"Matt", "Merin", "0490000002", "12345678",
            "Melbourne", 1, 5.0F, "MattMerinOnTheBeat@gmail.com"))
        db.insertUser(User(5,"John", "Carlos", "0490000003", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User(6,"Patrick", "Beverly", "0490000004", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User(7,"Dwyane", "Wade", "0490000005", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User(8,"Michael", "Phelps", "0490000006", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User(9,"Vladimir", "Putin", "0490000007", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))
        db.insertUser(User(10,"James", "Harden", "0490000008", "12345678",
            "Melbourne", 1, 5.0F, "JohnCarlos@gmail.com"))

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
        db.insertRequest(Friend(14, 10, 3, time.getDate(), time.getTime(), 0))*/

        fEnter = findViewById(R.id.user_name)
        sEnter = findViewById(R.id.password)

        //Buttons
        val logBut: Button = findViewById(R.id.logBut)
        val forgotBut: Button = findViewById(R.id.forgotBut)
        val createBut: Button = findViewById(R.id.create_an_account_but)

        //"Sign In" listener
        logBut.setOnClickListener {
            doLogin()
        }

        //"Recovery" listener
        forgotBut.setOnClickListener {
            val intent = Intent(this, DataRecovery::class.java)
            startActivity(intent)
        }

        //"Sign Up" listener
        createBut.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

    }

    /**
     * @function doLogin() retrieves data from two EditText's (txtPhone - phone number, txtPassword - password)
     * If txtPhone equals to username and txtPassword equals to password, then HomeActivity will be started
     * Else 'Toast' message will be displayed
     * If checkBox ("remember me") will be checked, then username and password will be saved in SharedPreferences
     */
    private fun doLogin() {
        val txtPhone: String = fEnter.text.toString()
        val txtPassword: String = sEnter.text.toString()
        val user: User = db.findUserByPhoneAndPassword(txtPhone, txtPassword)

        if (user.id != -1) {
            //save username and password in SharedPreferences
            rememberMe(user)
            //show logout activity
            showHome()
        } else
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
    }

    /**
     * @function showHome() starts HomeActivity and sends username
     */

    private fun showHome() {
        val intent = Intent(this, UserHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * @function getUser() checks username and password.
     * If username or password contain something, then the logout activity will be opened directly
     */

    private fun getUser() {
        val pref: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val username: String = pref.getString(prefVar[3], "") as String
        val password: String = pref.getString(prefVar[4], "") as String
        val list: User = db.findUserByPhoneAndPassword(username, password)

        if (list.id != -1)
            showHome()
    }

    /**
     * @function rememberMe() saves username and password values in SharedPreferences
     */

    //rememberMe() function saves username and password in SharedPreferences
    private fun rememberMe(user: User) {
        getSharedPreferences(prefName, Context.MODE_PRIVATE)
            .edit()
            .putInt(prefVar[0], user.id)
            .putString(prefVar[1], user.firstName)
            .putString(prefVar[2], user.lastName)
            .putString(prefVar[3], user.phone)
            .putString(prefVar[4], user.password)
            .putString(prefVar[5], user.city)
            .putFloat(prefVar[6], user.rating)
            .putString(prefVar[7], user.email)
            .apply()
    }

}