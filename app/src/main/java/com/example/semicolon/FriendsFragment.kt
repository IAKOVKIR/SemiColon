package com.example.semicolon

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FriendsFragment.OnListFragmentInteractionListener] interface.
 */
class FriendsFragment : Fragment() {

    private var columnCount = 1
    private var param1 : ArrayList<String>? = null

    private var listener: OnListFragmentInteractionListener? = null
    private var data: DatabaseOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            param1 = it.getStringArrayList("user")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends_list, container, false)
        data = context?.let { DatabaseOpenHelper(it) }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyFriendsRecyclerViewAdapter(data!!.readAllRequests(param1!![0]), listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onResume() {
        super.onResume()
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                return if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    val args = Bundle()
                    args.putStringArrayList("user", param1)

                    val fragment: Fragment = MainFragment()
                    fragment.arguments = args
                    val manager = fragmentManager
                    val transaction = manager!!.beginTransaction()
                    transaction.replace(R.id.nav_host, fragment)
                    // Commit the transaction
                    transaction.commit()

                    true
                } else
                    false
            }
        })
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
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: User?)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        /*@JvmStatic
        fun newInstance(columnCount: Int) =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
            */
    }
}