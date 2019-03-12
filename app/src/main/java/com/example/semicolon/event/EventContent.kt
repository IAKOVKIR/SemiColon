package com.example.semicolon.event

import com.example.semicolon.R
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object EventContent {

    /**
     * An array of sample (event) items.
     */
    val EVENTS: MutableList<Event> = ArrayList()

    private val event_names = arrayOf("Meeting with Amunil", "Talking about Inner join", "Global Query", "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT",
        "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT")

    private val event_details = arrayOf("2 persons joined the event", "2 persons joined the event", "4 persons joined the event", "Steven joined the event",
        "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event")

    private val images : Array<Int> = arrayOf(R.drawable.meeting_with_aminul, R.drawable.date_with_aminul, R.drawable.control_the_planet, R.drawable.burnes,
        R.drawable.burnes, R.drawable.burnes, R.drawable.burnes, R.drawable.burnes, R.drawable.burnes, R.drawable.burnes)

    /**
     * A map of sample (event) items, by ID.
     */
    private val EVENT_MAP: MutableMap<String, Event> = HashMap()

    private const val COUNT = 10

    init {
        // Add some sample items.
        for (i in 1..COUNT)
            addItem(createEvent(i))
    }

    private fun addItem(event : Event) {
        EVENTS.add(event)
        EVENT_MAP[event.id] = event
    }

    private fun createEvent(position: Int): Event {
        return Event(position.toString(), event_names[position - 1], makeDetails(position), images[position - 1])
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append(event_details[position - 1])
        return builder.toString()
    }

    /**
     * An Event item representing a piece of content.
     */
    data class Event(val id: String, val content: String, val details: String, val image: Int) {
        override fun toString(): String = content
    }
}
