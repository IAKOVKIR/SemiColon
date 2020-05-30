package com.example.semicolon.following_followers

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.semicolon.R
import com.example.semicolon.databinding.PublicFollowersFollowingFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g MY_ID, USER_ID, EXCEPTION_ID and SLIDE_NUMBER
private const val MY_ID = "my_id"
private const val USER_ID = "user_id"

class PublicFollowersFollowingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: PublicFollowersFollowingFragmentBinding = DataBindingUtil.inflate(
                    inflater, R.layout.public_followers_following_fragment, container, false)

        val args = PublicFollowersFollowingFragmentArgs.fromBundle(requireArguments())
        val myID: Int = args.myId //myID
        val userID: Int = args.userId //myID

        val viewpager: ViewPager2 = binding.viewpager
        val tabs: TabLayout = binding.tabs
        val backButton: TextView = binding.backButton

        tabs.setBackgroundColor(Color.WHITE)
        tabs.setTabTextColors(ContextCompat.getColor(requireContext(), R.color.SPECIAL), ContextCompat.getColor(requireContext(), R.color.BLUE))
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#1D98A7"))

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

        tabs.getTabAt(args.slideNumber)!!.select()

        backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getTab(my_id: Int, user_id: Int, pos: Int): Fragment {
        val args = Bundle()
        args.putInt(MY_ID, my_id)
        args.putInt(USER_ID, user_id)
        val f = when (pos) {
            0 -> ListMutual()
            1 -> ListFollowers()
            else -> ListFollowing()
        }
        f.arguments = args
        return f
    }

    internal inner class ViewPagerAdapter(fr: Fragment, my_id: Int, user_id: Int) : FragmentStateAdapter(fr) {

        private val myID = my_id
        private val userID = user_id

        override fun createFragment(position: Int): Fragment = when (position) {
            0, 1, 2 -> getTab(myID, userID, position)
            else -> throw IllegalStateException("Invalid adapter position")
        }
        override fun getItemCount(): Int = 3
    }
}