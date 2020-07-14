package com.example.semicolon.main_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.semicolon.R
import com.example.semicolon.databinding.FragmentMainBinding
import com.example.semicolon.main_fragments.view_models.MainFragmentViewModel
import com.example.semicolon.main_fragments.view_models.MainFragmentViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var userID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val n: SharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        userID = n.getInt("id", -1)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false)

        val application = requireNotNull(this.activity).application

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the ViewModel
        val viewModelFactory =
            MainFragmentViewModelFactory(
                userID,
                userDataSource,
                followerDataSource,
                application
            )
        val viewModel: MainFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainFragmentViewModel::class.java)

        binding.apply {
            // Set the ViewModel for data binding - this allows the bound layout access to all of the
            // data in the ViewModel
            mainFragmentViewModel = viewModel
            // Specify the current activity as the lifecycle owner of the binding. This is used so that
            // the binding can observe LiveData updates
            lifecycleOwner = viewLifecycleOwner

            //followers layout
            linearLayoutFollowers.setOnClickListener {view: View ->
                view.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToFollowingFollowersFragment(
                        userID,
                        userID,
                        userID,
                        0
                    )
                )
            }

            //following layout
            linearLayoutFollowing.setOnClickListener {view: View ->
                view.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToFollowingFollowersFragment(
                        userID,
                        userID,
                        userID,
                        1
                    )
                )
            }

            // settings ImageButton
            settingsButton.setOnClickListener {view: View ->
                view.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
            }
        }

        //Inflate the layout for this fragment
        return binding.root
    }
}