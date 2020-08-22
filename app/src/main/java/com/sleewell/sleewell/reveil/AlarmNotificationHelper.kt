package com.sleewell.sleewell.reveil

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.sleewell.sleewell.reveil.View.AlarmActivity
import com.sleewell.sleewell.R

/**
 * Notification helper of the application
 *
 * @param base Context of the application
 */
class AlarmNotificationHelper(base: Context?) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null

    /**
     * Create channel for the notification
     *
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    val channelNotification: NotificationCompat.Builder
        get() {
            val stopIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
                action = "Stop"
            }
            val stopPendingIntent = PendingIntent.getBroadcast(applicationContext, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val snoozeIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
                action = "Snooze"
            }
            val snoozePendingIntent = PendingIntent.getBroadcast(applicationContext, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val intent = Intent(this, AlarmActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            return NotificationCompat.Builder(applicationContext, channelID)
                    .setContentTitle("Sleewell")
                    .setContentText("It's time to wake up !")
                    .setSmallIcon(R.drawable.logo_sleewell)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.logo_sleewell, "Stop", stopPendingIntent)
                    .addAction(R.drawable.logo_sleewell, "Snooze", snoozePendingIntent)
        }

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}