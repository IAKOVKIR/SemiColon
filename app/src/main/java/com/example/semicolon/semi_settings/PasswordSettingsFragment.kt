package com.example.semicolon.semi_settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.semicolon.R
import com.example.semicolon.models.DBContract
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameter, e.g MY_ID
private const val MY_ID = "my_id"

/**
 * A simple [Fragment] subclass.
 */
class PasswordSettingsFragment : Fragment() {

    private var myID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myID = it.getInt(MY_ID) //myID
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_password_settings, container, false)
        val db = DatabaseOpenHelper(requireContext())

        //EditText's
        val currentPassword: EditText = view.findViewById(R.id.current_password)
        val newPassword: EditText = view.findViewById(R.id.new_password)
        val repeatPassword: EditText = view.findViewById(R.id.repeat_password)

        //Buttons
        val buttonSubmit: Button = view.findViewById(R.id.submit_button)
        val backButton: TextView = view.findViewById(R.id.back_button)

        //onClickListener
        buttonSubmit.setOnClickListener {
            val password: String? = db.getUsersData(myID!!, DBContract.UserEntry.USER_COLUMN_PASSWORD)
            val curPas: String = currentPassword.text.toString()
            val newPas: String = newPassword.text.toString()

            if (password == curPas)
                if (newPas == repeatPassword.text.toString())
                    if (newPas != curPas) {
                        CoroutineScope(Dispatchers.Default).launch {

                            withContext(Dispatchers.Default) {
                                db.setPassword(myID!!, newPas)
                                changePassword(newPas)
                            }

                            launch (Dispatchers.Main) {
                                // process the data on the UI thread
                                closeFragment()
                            }
                        }
                    } else
                        Toast.makeText(context, "New password and current password are equal", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "New passwords are not equal", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Wrong current password", Toast.LENGTH_LONG).show()
        }

        backButton.setOnClickListener {
            closeFragment()
        }

        return view

    }

    private fun closeFragment() {
        val fm: FragmentManager = parentFragmentManager
        fm.popBackStack("to_password_settings", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    //changePassword() function saves a new password in SharedPreferences
    private fun changePassword(password: String) {
        requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            .edit()
            .putString("password", password)
            .apply()
    }

}