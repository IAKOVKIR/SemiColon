package com.example.semicolon

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingParamsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingParamsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var log = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting_params, container, false)
        val fullText = "$param1"
        val nameText = view.findViewById<TextView>(R.id.name_text)
        val passwordLayout = view.findViewById<LinearLayout>(R.id.password_layout)
        val notificationLayout = view.findViewById<LinearLayout>(R.id.notification_layout)
        nameText.text = fullText

        if (param2 == "1") {
            notificationLayout.visibility = View.VISIBLE
        } else if (param2 == "2") {
            passwordLayout.visibility = View.VISIBLE
            val currentPassword = view.findViewById<EditText>(R.id.current_password)
            val newPassword = view.findViewById<EditText>(R.id.new_password)
            val repeatPassword = view.findViewById<EditText>(R.id.repeat_password)

            val buttonSubmit = view.findViewById<Button>(R.id.submit_button)
            buttonSubmit.setOnClickListener {
                val pref = context!!.getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
                val password = pref.getString(log.prefPassword, null)
                val curPas = currentPassword.text.toString()
                val newPas = newPassword.text.toString()

                if (password == curPas)
                    if (newPas == repeatPassword.text.toString())
                        if (newPas != curPas) {
                            changePassword(newPas)
                            activity!!.onBackPressed()
                        } else
                            Toast.makeText(context, "New password and current password are equal", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(context, "New passwords are not equal", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Wrong current password", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    //changePassword() function saves a new password in SharedPreferences
    private fun changePassword(password: String) {
        context!!.getSharedPreferences(log.prefName, Context.MODE_PRIVATE)
            .edit()
            .putString(log.prefPassword, password)
            .apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingParamsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingParamsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}