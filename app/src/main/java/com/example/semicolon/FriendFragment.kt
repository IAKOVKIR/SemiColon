package com.example.semicolon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.semicolon.databinding.FragmentFriendBinding
import com.example.semicolon.following_followers.view_models.FriendFragmentViewModel
import com.example.semicolon.following_followers.view_models.FriendFragmentViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FriendFragment : Fragment() {

    private var myID: Int? = null
    private var userID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = FriendFragmentArgs.fromBundle(requireArguments())
            myID = args.myId //myID
            userID = args.userId //userID
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentFriendBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_friend, container, false)

        val application = requireNotNull(this.activity).application

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the ViewModel
        val viewModelFactory = FriendFragmentViewModelFactory(myID!!, userID!!, userDataSource, followerDataSource, application)
        val viewModel: FriendFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FriendFragmentViewModel::class.java)

        // Set the ViewModel for data binding - this allows the bound layout access to all of the
        // data in the ViewModel
        binding.friendFragmentViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //Layouts
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

        //OnClickListener's
        binding.followUnFollowButton.setOnClickListener{
            if (myID != null && userID != null)
                viewModel.followOrUnFollow(myID!!, userID!!)
        }

        binding.followedBy.setOnClickListener {
            this.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 0))
        }

        binding.linearLayoutFollowers.setOnClickListener {
            this.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 1))
        }

        binding.linearLayoutFollowing.setOnClickListener {
            this.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 2))
        }

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}