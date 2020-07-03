package com.example.semicolon

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.semicolon.databinding.FragmentMainBinding
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
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
            inflater, R.layout.fragment_main, container, false)
        val db = DatabaseOpenHelper(requireContext())

        val application = requireNotNull(this.activity).application

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the viewmodel
        val viewModelFactory = MainFragmentViewFactory(userID, userDataSource, followerDataSource, application)
        val viewModel: MainFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainFragmentViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.mainFragmentViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        CoroutineScope(Dispatchers.Default).launch {

            lateinit var bitmapDrawable: BitmapDrawable

            withContext(Dispatchers.Default) {
                var bitmap: Bitmap = BitmapFactory.decodeResource(binding.root.resources, R.drawable.burns)
                val dif: Double = bitmap.height.toDouble() / bitmap.width
                bitmap = Bitmap.createScaledBitmap(bitmap, 180, (180 * dif).toInt(), true)
                bitmapDrawable = BitmapDrawable(requireContext().resources, bitmap)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                // CircleViewImage
                binding.circleImageView.setImageDrawable(bitmapDrawable)
            }

        }

        CoroutineScope(Dispatchers.Default).launch {

            var following = 0

            withContext(Dispatchers.Default) {
                following = db.countFollowing(userID)
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread

                //TextView representing the number of users you follow
                binding.followingNumber.text = "$following"
            }

        }

        //followers layout
        binding.linearLayoutFollowers.setOnClickListener { view: View ->
            view.findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToFollowingFollowersFragment(userID, userID, userID, 0))
        }

        //following layout
        binding.linearLayoutFollowing.setOnClickListener {view: View ->
            view.findNavController().navigate(MainFragmentDirections
                .actionMainFragmentToFollowingFollowersFragment(userID, userID, userID, 1))
        }

        // settings ImageButton
        binding.settingsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
        }

        //Inflate the layout for this fragment
        return binding.root
    }
}