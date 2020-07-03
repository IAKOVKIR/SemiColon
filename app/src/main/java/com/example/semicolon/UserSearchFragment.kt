package com.example.semicolon

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.semicolon.databinding.FragmentUserSearchBinding
import com.example.semicolon.models.User
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import kotlinx.coroutines.*

// the fragment initialization parameters, e.g MY_ID, USER_ID and EXCEPTION_ID
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [UserSearchFragment.OnListFragmentInteractionListener] interface.
 */

class UserSearchFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    lateinit var db: DatabaseOpenHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentUserSearchBinding = DataBindingUtil.inflate(
                            inflater, R.layout.fragment_user_search, container, false)
        //EditTexts
        val editText: EditText = binding.editText

        val list: RecyclerView = binding.list
        db = DatabaseOpenHelper(requireContext())
        val listUser: ArrayList<User> = ArrayList()

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val prefix = "> "
                if (!s.toString().startsWith(prefix)) {
                    val deletedPrefix: String = prefix.substring(0, prefix.length - 1)
                    var cleanString: String = if (s.toString().startsWith(deletedPrefix))
                        s.toString().replace(Regex(deletedPrefix), "")
                    else
                        s.toString().replace(Regex(prefix), "")

                    CoroutineScope(Dispatchers.Default).launch {

                        listUser.addAll(withContext(Dispatchers.Default) { load() })

                        CoroutineScope(Dispatchers.Main).launch {
                            with(list) {
                                adapter!!.notifyDataSetChanged()
                            }
                        }

                    }

                    cleanString = "$prefix$cleanString"
                    editText.setText(cleanString)
                    editText.setSelection(cleanString.length)
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        // Set the adapter
        with(list) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyUserSearchRecyclerViewAdapter(
                listUser,
                listener as OnListFragmentInteractionListener)
            setHasFixedSize(true)
        }

        CoroutineScope(Dispatchers.Default).launch {

            listUser.addAll(withContext(Dispatchers.Default) { load() })

            CoroutineScope(Dispatchers.Main).launch {
                with(list) {
                    adapter!!.notifyDataSetChanged()
                }
            }

        }

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }

    private fun load() : ArrayList<User> {
        return db.readFirstTenUsers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener)
            listener = context
        else
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }
}