package com.example.semicolon.dummy

import com.example.semicolon.User
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object Friends {

    /**
     * An array of sample (Friends) items.
     */
    val FRIEND: MutableList<User> = ArrayList()

    /**
     * A map of sample (Friends) items, by ID.
     */
    val FRIEND_MAP: MutableMap<Int, User> = HashMap()

    private val COUNT = 5

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            addItem(createFriendItem(i))
    }

    private fun addItem(item: User) {
        FRIEND.add(item)
        FRIEND_MAP[item.id] = item
    }

    private fun createFriendItem(position: Int): User {
        return User(1, "", "", "21/03/2019", "16:52:36", "inProgress", 2, 5.0F, "")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        return builder.toString()
    }

}