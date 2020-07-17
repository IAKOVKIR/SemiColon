package com.example.semicolon.following_followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentRequestsBinding
import com.example.semicolon.following_followers.view_models.RequestsFragmentViewModel
import com.example.semicolon.following_followers.view_models.RequestsFragmentViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class RequestsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentRequestsBinding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_requests, container, false)
        val listUser: ArrayList<User> = ArrayList()

        val application = requireNotNull(this.activity).application
        val args = RequestsFragmentArgs.fromBundle(requireArguments())
        //Stores the id of the user that signed in
        val userId: Int = args.userId

        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        val viewModelFactory = RequestsFragmentViewModelFactory(userId, followerDataSource, application)
        val testViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(RequestsFragmentViewModel::class.java)

        testViewModel.userId.observe(viewLifecycleOwner, Observer { selectedUserId ->
            selectedUserId?.let {
                this.findNavController().navigate(RequestsFragmentDirections.actionRequestsFragmentToFriendFragment(userId, selectedUserId))
                testViewModel.onSelectedUserNavigated()
            }
        })

        val adapter = RequestsRecyclerViewAdapter(userId, testViewModel, listUser, application)
        binding.list.adapter = adapter

        testViewModel.totalRequests.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (listUser.isEmpty()) {
                    listUser.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}