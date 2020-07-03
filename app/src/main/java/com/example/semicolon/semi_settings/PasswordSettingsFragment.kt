package com.example.semicolon.semi_settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentPasswordSettingsBinding
import com.example.semicolon.models.DBContract
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class PasswordSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentPasswordSettingsBinding = DataBindingUtil.inflate(
                            inflater, R.layout.fragment_password_settings, container, false)
        val db = DatabaseOpenHelper(requireContext())

        val args = PasswordSettingsFragmentArgs.fromBundle(requireArguments())
        val myID: Int = args.myId //myID

        //onClickListener
        binding.submitButton.setOnClickListener {
            val password: String? = db.getUsersData(myID, DBContract.UserEntry.USER_COLUMN_PASSWORD)
            val curPas: String = binding.currentPassword.text.toString()
            val newPas: String = binding.newPassword.text.toString()

            if (password == curPas)
                if (newPas == binding.repeatPassword.text.toString())
                    if (newPas != curPas) {
                        CoroutineScope(Dispatchers.Default).launch {

                            withContext(Dispatchers.Default) {
                                db.setPassword(myID, newPas)
                                changePassword(newPas)
                            }

                            launch (Dispatchers.Main) {
                                // process the data on the UI thread
                                binding.backButton.setOnClickListener {view: View ->
                                    view.findNavController().popBackStack()
                                }
                            }
                        }
                    } else
                        Toast.makeText(context, "New password and current password are equal", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "New passwords are not equal", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Wrong current password", Toast.LENGTH_LONG).show()
        }

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }

    //changePassword() function saves a new password in SharedPreferences
    private fun changePassword(password: String) {
        requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            .edit()
            .putString("password", password)
            .apply()
    }
}