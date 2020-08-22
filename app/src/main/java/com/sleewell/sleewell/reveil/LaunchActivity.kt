package com.sleewell.sleewell.reveil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.R

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        stopAlarm(applicationContext)
    }

    /**
     * Stop the alert
     *
     * @param context Context of the application
     */
    private fun stopAlarm(context: Context?) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(1)
        notificationManager.cancelAll()
    }
}