package com.example.semicolon

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.*
import android.widget.DatePicker
import android.view.Gravity
import android.graphics.Color
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var seconds: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get current calendar date and time.
        val currCalendar = Calendar.getInstance()

        // Set the timezone which you want to display time.
        currCalendar.timeZone = TimeZone.getTimeZone("Australia/Sydney")

        year = currCalendar.get(Calendar.YEAR)
        month = currCalendar.get(Calendar.MONTH)
        day = currCalendar.get(Calendar.DAY_OF_MONTH)
        hour = currCalendar.get(Calendar.HOUR_OF_DAY)
        minute = currCalendar.get(Calendar.MINUTE)
        seconds = currCalendar.get(Calendar.SECOND)

        showUserSelectDateTime()

        // Get date picker object.
        val datePicker = findViewById<DatePicker>(R.id.datePickerExample)

        datePicker.init(
            year - 2, month + 1, day + 5
        ) { _, year, month, day ->
            this@MainActivity.year= year
            this@MainActivity.month = month
            this@MainActivity.day = day

            showUserSelectDateTime()
        }

        val b = findViewById<Button>(R.id.button)
        // when button is clicked, show the alert
        b.setOnClickListener {
            // build alert dialog
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Do you want to close this application ?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Proceed") { _, _ -> finish()}
                // negative button text and action
                .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()
                }


            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("AlertDialogExample")
            // show alert dialog
            alert.show()

        }
    }

    /* Show user selected date time in bottom text vew area. */
    private fun showUserSelectDateTime() {
        // Get TextView object which is used to show user pick date and time.

        val textView = findViewById<TextView>(R.id.textt)

        val strBuffer = StringBuffer()
        strBuffer.append("You selected date time : ")
        strBuffer.append(this.year)
        strBuffer.append("-")
        strBuffer.append(this.month + 1)
        strBuffer.append("-")
        strBuffer.append(this.day)

        textView.text = strBuffer.toString()
        textView.setTextColor(Color.BLUE)
        textView.gravity = Gravity.CENTER
        textView.textSize = 20f
    }

}