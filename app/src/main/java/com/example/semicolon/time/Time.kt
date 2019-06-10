package com.example.semicolon.time

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Time {

    fun getTime(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate: String = sdf.format(c.time).trim()
        return strDate.substring(0, 10)
    }

    fun getDate(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val strDate: String = sdf.format(c.time).trim()
        return strDate.substring(11, 19)
    }
}