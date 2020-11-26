package com.sleewell.sleewell.reveil

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import kotlinx.android.synthetic.main.activity_desactivation_alarm.*

class DesactivationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desactivation_alarm)

        val stopIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
            action = "Stop"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(applicationContext, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        swipe_button.setOnStateChangeListener {
            stopPendingIntent.send()
            finish()
        }
    }

}