package com.example.semicolon.support_features

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Time {

    fun getDate(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MMM/dd", Locale.getDefault())
        return sdf.format(c.time).trim()
    }

    fun getTime(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(c.time).trim()
    }
}