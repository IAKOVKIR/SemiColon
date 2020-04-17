package com.example.semicolon.models

object EventContent {

    /**
     * An array of sample (event) items.
     */
    /*val EVENTS: MutableList<Event> = ArrayList()

    private val event_names: Array<String> = arrayOf("Meeting with Aminul", "Talking about Inner join", "Global Query", "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT",
        "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT", "Sleep at RMIT")

    private val event_details: Array<String> = arrayOf("2 persons joined the event", "2 persons joined the event", "4 persons joined the event", "Steven joined the event",
        "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event", "Steven joined the event")

    private val images : Array<Int> = arrayOf(R.drawable.plan_with_aminul, R.drawable.date_with_aminul, R.drawable.control_the_planet, R.drawable.burns,
        R.drawable.burns, R.drawable.burns, R.drawable.burns, R.drawable.burns, R.drawable.burns, R.drawable.burns)

    /**
     * A map of sample (event) items, by ID.
     */
    private val EVENT_MAP: MutableMap<String, Event> = HashMap()

    private const val COUNT = 10

    init {
        // Add some sample items.
        for (i: Int in 1..COUNT)
            addItem(createEvent(i))
        addItem(createEvent(COUNT))
    }

    fun getList(frag: String): MutableList<Event> {
        val searchEvents: MutableList<Event> = ArrayList()
        for (i: Int in 1..COUNT) {
            val item: Event = createEvent(i)
            if (item.content.contains(frag))
                searchEvents.add(item)
        }
        return searchEvents
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
     * An Event item representing a piece of pos.
     */*/
    data class Event(val eventId: Int, val eventName: String, val maxAttendees: Int, val location: String, val startDate: String, val endDate: String, val startTime: String, val endTime: String) {
        constructor(): this(-1, "-1", -1, "-1", "-1", "-1", "-1", "-1")
        constructor(maxAttendees: Int, eventName: String, location: String, startDate: String, endDate: String, startTime: String, endTime: String):
                this (-1, eventName, maxAttendees, location, startDate, endDate, startTime, endTime)
    }
}
