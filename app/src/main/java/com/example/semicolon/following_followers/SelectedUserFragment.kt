package com.example.semicolon.following_followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentSelectedUserBinding
import com.example.semicolon.following_followers.view_models.SelectedUserFragmentViewModel
import com.example.semicolon.following_followers.view_models.SelectedUserFragmentViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SelectedUserFragment : Fragment() {

    private var userID: Int? = null
    private var selectedUserID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = SelectedUserFragmentArgs.fromBundle(requireArguments())
        userID = args.userId
        selectedUserID = args.selectedUserId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSelectedUserBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_selected_user, container, false)

        val application = requireNotNull(this.activity).application

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the ViewModel
        val viewModelFactory = SelectedUserFragmentViewModelFactory(userID!!, selectedUserID!!, userDataSource, followerDataSource, application)
        val viewModel: SelectedUserFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SelectedUserFragmentViewModel::class.java)

        // Set the ViewModel for data binding - this allows the bound layout access to all of the
        // data in the ViewModel
        binding.selectedUserFragmentViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //Layouts
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

        //OnClickListener's
        binding.followUnFollowButton.setOnClickListener{
            if (selectedUserID != null && userID != null)
                viewModel.followOrUnFollow(userID!!, selectedUserID!!)
        }

        binding.followedBy.setOnClickListener {
            this.findNavController().navigate(
                SelectedUserFragmentDirections.actionFriendFragmentToPublicFollowersFollowingFragment(
                    userID!!,
                    selectedUserID!!,
                    0
                )
            )
        }

        binding.linearLayoutFollowers.setOnClickListener {
            this.findNavController().navigate(
                SelectedUserFragmentDirections.actionFriendFragmentToPublicFollowersFollowingFragment(
                    userID!!,
                    selectedUserID!!,
                    1
                )
            )
        }

        binding.linearLayoutFollowing.setOnClickListener {
            this.findNavController().navigate(
                SelectedUserFragmentDirections.actionFriendFragmentToPublicFollowersFollowingFragment(
                    userID!!,
                    selectedUserID!!,
                    2
                )
            )
        }

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}