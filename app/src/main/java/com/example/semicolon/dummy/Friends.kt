package com.example.semicolon.dummy

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
    val FRIEND: MutableList<FriendItem> = ArrayList()

    /**
     * A map of sample (Friends) items, by ID.
     */
    val FRIEND_MAP: MutableMap<String, FriendItem> = HashMap()

    private val COUNT = 5

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            addItem(createFriendItem(i))
    }

    private fun addItem(item: FriendItem) {
        FRIEND.add(item)
        FRIEND_MAP[item.id] = item
    }

    private fun createFriendItem(position: Int): FriendItem {
        return FriendItem("Aminul", "Islam", position.toString(), "Item $position", makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class FriendItem(val firstName: String, val secondName: String, val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
