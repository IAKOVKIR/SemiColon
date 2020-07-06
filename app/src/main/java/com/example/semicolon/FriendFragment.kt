package com.example.semicolon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.semicolon.databinding.FragmentFriendBinding
import com.example.semicolon.following_followers.view_models.FriendFragmentViewModel
import com.example.semicolon.following_followers.view_models.FriendFragmentViewModelFactory
import com.example.semicolon.sqlite_database.AppDatabase
import com.example.semicolon.sqlite_database.DatabaseOpenHelper
import com.example.semicolon.sqlite_database.Follower
import com.example.semicolon.support_features.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendFragment : Fragment() {

    private var myID: Int? = null
    private var userID: Int? = null
    lateinit var db: DatabaseOpenHelper
    //Array str contains 3 of these conditions
    private val str: Array<String> = arrayOf("follow", "in progress", "unfollow")//-1, 0, 1
    //Time object
    val time = Time()

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
        //DatabaseOpenHelper object
        db = DatabaseOpenHelper(requireContext())

        val application = requireNotNull(this.activity).application

        val userDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).userDao
        val followerDataSource = AppDatabase.getInstance(application, CoroutineScope(Dispatchers.Main)).followerDao

        // Get the ViewModel
        val viewModelFactory = FriendFragmentViewModelFactory(userID!!, userDataSource, followerDataSource, application)
        val viewModel: FriendFragmentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(FriendFragmentViewModel::class.java)

        // Set the ViewModel for data binding - this allows the bound layout access to all of the
        // data in the ViewModel
        binding.friendFragmentViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //TextViews
        val followedBy: TextView = binding.followedBy
        val followUnFollowButton: TextView = binding.followUnFollowButton

        //Layouts
        //val eventsLayout: LinearLayout = findViewById(R.id.linear_layout_following)

        var bool = 0

        CoroutineScope(Dispatchers.Default).launch {
            var followedByList: ArrayList<String>
            var followedByLine = ""
            //var followers = 0
            //var following = 0

            withContext(Dispatchers.Default) {
                //followers = db.countFollowers(userID!!)
                //following = db.countFollowing(userID!!)
                bool = db.checkFollower(myID!!, userID!!)
                followedByList = db.readFirstThreeMutualFollowers(myID!!, userID!!)

                val len: Int = followedByList.size
                followedByLine = when {
                    len == 3 -> "Followed by <b>${followedByList[0]}</b>, <b>${followedByList[1]}</b> and <b>1 other</b>"
                    len == 2 -> "Followed by <b>${followedByList[0]}</b> and <b>${followedByList[1]}</b>"
                    len == 1 -> "Followed by <b>${followedByList[0]}</b>"
                    len > 3 -> "Followed by <b>${followedByList[0]}</b>, <b>${followedByList[1]}</b> and <b>${len - 2} others</b>"
                    else -> ""
                }
            }

            launch (Dispatchers.Main) {
                // process the data on the UI thread
                //binding.followersNumber.text = "$followers"
                //binding.followingNumber.text = "$following"
                followUnFollowButton.text = str[bool + 1]
                followedBy.isEnabled = true
                followedBy.text = HtmlCompat.fromHtml(followedByLine, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        }

        /*
        Variable bool contains one of three conditions of "friendship":
        "1" - you follow the user
        "0" - "your request is in progress"
        "-1" - "you do not follow the user"
        */

        followUnFollowButton.setOnClickListener{

            if (bool == -1) {
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        val friend = Follower(
                            myID!!, userID!!, 0,
                            time.toString(), time.toString()
                        )
                        res = db.insertRequest(friend)
                    }

                    launch(Dispatchers.Main) {
                        if (res) {
                            followUnFollowButton.text = str[1]
                            bool = 0
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Default).launch {

                    var res = false

                    withContext(Dispatchers.Default) {
                        res = db.deleteFollowing(myID!!, userID!!)
                    }

                    launch(Dispatchers.Main) {
                        if (res) {
                            followUnFollowButton.text = str[2]
                            bool = 1
                        }
                    }
                }
            }
        }

        //OnClickListener's
        followedBy.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 0))
        }

        binding.linearLayoutFollowers.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 1))
        }

        binding.linearLayoutFollowing.setOnClickListener {view: View ->
            view.findNavController().navigate(FriendFragmentDirections
                .actionFriendFragmentToPublicFollowersFollowingFragment(myID!!, userID!!, 2))
        }

        binding.backButton.setOnClickListener {view: View ->
            view.findNavController().popBackStack()
        }

        return binding.root
    }
}