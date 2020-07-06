package com.example.semicolon.following_followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        val viewModelFactory = RequestsFragmentViewModelFactory(args.myId, userDataSource, followerDataSource)
        val testViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(RequestsFragmentViewModel::class.java)

        testViewModel.userId.observe(viewLifecycleOwner, Observer { userId ->
            userId?.let {
                this.findNavController().navigate(RequestsFragmentDirections.actionRequestsFragmentToFriendFragment(args.myId, userId))
                testViewModel.onSleepDataQualityNavigated()
            }
        })

        val adapter = RequestsRecyclerViewAdapter(testViewModel, listUser)
        binding.list.adapter = adapter

        testViewModel.totalRequests.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (listUser.isEmpty()) {
                    listUser.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }
}