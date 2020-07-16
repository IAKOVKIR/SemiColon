package com.example.semicolon.following_followers

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.semicolon.*
import com.example.semicolon.databinding.FollowingFollowersFragmentBinding
import com.example.semicolon.following_followers.view_models.FollowingFollowersViewModel
import com.example.semicolon.following_followers.view_models.FollowingFollowersViewModelFactory
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowers
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowing
import com.example.semicolon.sqlite_database.AppDatabase
import kotlinx.coroutines.*
import com.google.android.material.tabs.TabLayoutMediator

class FollowingFollowersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FollowingFollowersFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.following_followers_fragment, container, false)

        val args = FollowingFollowersFragmentArgs.fromBundle(requireArguments())
        //Stores the id of the user that signed in
        val userId: Int = args.userId

        val viewpager = binding.viewpager
        val tabs = binding.tabs

        val application = requireNotNull(this.activity).application
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the ViewModel
        val viewModelFactory =
            FollowingFollowersViewModelFactory(userId, followerDataSource)
        val viewModel: FollowingFollowersViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FollowingFollowersViewModel::class.java)

        tabs.apply {
            setBackgroundColor(Color.WHITE)
            setTabTextColors(
                ContextCompat.getColor(requireContext(), R.color.SPECIAL),
                ContextCompat.getColor(requireContext(), R.color.BLUE)
            )
            setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))
        }

        val viewPagerAdapter = ViewPagerAdapter(this, userId)
        var selectedTabPosition = 0
        viewpager.apply {
            adapter = viewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectedTabPosition = position
                }
            })
            currentItem = selectedTabPosition
        }

        // New way of interaction with TabLayout and page title setting
        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                else -> "Following"
            }
        }.attach()

        tabs.getTabAt(args.slideNumber)!!.select()

        binding.apply {
            // Set the ViewModel for data binding - this allows the bound layout access to all of the
            // data in the ViewModel
            followingFollowersFragmentViewModel = viewModel
            // Specify the current activity as the lifecycle owner of the binding. This is used so that
            // the binding can observe LiveData updates
            lifecycleOwner = viewLifecycleOwner

            requestsButton.setOnClickListener { view: View ->
                view.findNavController().navigate(
                    FollowingFollowersFragmentDirections
                        .actionFollowingFollowersFragmentToRequestsFragment(userId)
                )
            }

            backButton.setOnClickListener { view: View ->
                view.findNavController().popBackStack()
            }
        }

        return binding.root
    }

    private fun getTab(user_id: Int, pos: Int): Fragment {
        val args = Bundle()
        args.putInt("user_id", user_id)
        args.putInt("selected_user_id", user_id)

        val f = if (pos == 0)
            ListFollowers()
        else
            ListFollowing()
        f.arguments = args
        return f
    }

    internal inner class ViewPagerAdapter(fr: Fragment, private val user_id: Int) : FragmentStateAdapter(fr) {

        override fun createFragment(position: Int): Fragment = when (position) {
            0, 1 -> getTab(user_id, position)
            else -> throw IllegalStateException("Invalid adapter position")
        }

        override fun getItemCount(): Int = 2
    }
}