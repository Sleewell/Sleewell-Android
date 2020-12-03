package com.sleewell.sleewell.reveil

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import kotlinx.android.synthetic.main.activity_desactivation_alarm.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DesactivationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivation_alarm)

        val currentDateTime = LocalDateTime.now()
        val time = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        time_desactivation.text = time

        val stopIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
            action = "Stop"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(applicationContext, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        swipe_button.setOnStateChangeListener {
            stopPendingIntent.send()
            finish()
        }

        val snoozeIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
            action = "Snooze"
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(applicationContext, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        snooze_desactivation.setOnClickListener {
            snoozePendingIntent.send()
            finish()
        }
    }

}