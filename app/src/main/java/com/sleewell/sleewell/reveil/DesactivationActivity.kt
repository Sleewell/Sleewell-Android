package com.sleewell.sleewell.reveil

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.data.model.Alarm
import kotlinx.android.synthetic.main.activity_desactivation_alarm.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DesactivationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivation_alarm)

        val alarm: Alarm

        val bundle = intent.getBundleExtra("ALARM")
        if (bundle != null) {
            alarm = bundle.getParcelable("alarm")!!

            val currentDateTime = LocalDateTime.now()
            val time = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            time_desactivation.text = time

            val stopIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
                action = "Stop"
            }

            val stopBundle = Bundle()
            stopBundle.putParcelable("alarm", alarm)
            stopIntent.putExtra("ALARM", stopBundle)

            val stopPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                alarm.id,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            swipe_button.setOnStateChangeListener {
                stopPendingIntent.send()
                finish()
            }

            val snoozeIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
                action = "Snooze"
            }

            val snoozeBundle = Bundle()
            snoozeBundle.putParcelable("alarm", alarm)
            snoozeIntent.putExtra("ALARM", stopBundle)

            val snoozePendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                alarm.id,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            snooze_desactivation.setOnClickListener {
                snoozePendingIntent.send()
                finish()
            }
        }
    }
}