package com.example.semicolon.following_followers

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.semicolon.R
import com.example.semicolon.databinding.PublicFollowersFollowingFragmentBinding
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowers
import com.example.semicolon.following_followers.viewpager_fragments.ListFollowing
import com.example.semicolon.following_followers.viewpager_fragments.ListMutual
import com.google.android.material.tabs.TabLayoutMediator

class PublicFollowersFollowingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: PublicFollowersFollowingFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.public_followers_following_fragment, container, false)

        val args = PublicFollowersFollowingFragmentArgs.fromBundle(requireArguments())
        val myID: Int = args.myId //myID
        val userID: Int = args.userId //myID

        //ViewPager2
        val viewpager = binding.viewpager
        val tabs = binding.tabs

        tabs.apply {
            setBackgroundColor(Color.WHITE)
            setTabTextColors(
                ContextCompat.getColor(requireContext(), R.color.SPECIAL),
                ContextCompat.getColor(requireContext(), R.color.BLUE)
            )
            setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))
            getTabAt(args.slideNumber)!!.select()
        }

        val viewPagerAdapter = ViewPagerAdapter(this, myID, userID)
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
                0 -> "Mutual"
                1 -> "Followers"
                else -> "Following"
            }
        }.attach()

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getTab(my_id: Int, user_id: Int, pos: Int): Fragment {
        val args = Bundle()
        args.putInt("my_id", my_id)
        args.putInt("user_id", user_id)
        val f = when (pos) {
            0 -> ListMutual()
            1 -> ListFollowers()
            else -> ListFollowing()
        }
        f.arguments = args
        return f
    }

    internal inner class ViewPagerAdapter(fr: Fragment, private val my_id: Int, private val user_id: Int) : FragmentStateAdapter(fr) {

        override fun createFragment(position: Int): Fragment = when (position) {
            0, 1, 2 -> getTab(my_id, user_id, position)
            else -> throw IllegalStateException("Invalid adapter position")
        }
        override fun getItemCount(): Int = 3
    }
}