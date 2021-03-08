package com.sleewell.sleewell.reveil

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.data.model.Alarm

/**
 * Notification helper of the application
 *
 * @param base Context of the application
 * @author Romane Bézier
 */
class AlarmNotificationHelper(base: Context?, currentAlarm: Alarm) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null
    private var alarm: Alarm = currentAlarm

    /**
     * Create channel for the notification
     *
     * @author Romane Bézier
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
            val stopBundle = Bundle()
            stopBundle.putParcelable("alarm", alarm)
            stopIntent.putExtra("ALARM", stopBundle)
            val stopPendingIntent = PendingIntent.getBroadcast(applicationContext, alarm.id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val snoozeIntent = Intent(applicationContext, GlobalReceiver::class.java).apply {
                action = "Snooze"
            }
            val snoozeBundle = Bundle()
            snoozeBundle.putParcelable("alarm", alarm)
            snoozeIntent.putExtra("ALARM", snoozeBundle)
            val snoozePendingIntent = PendingIntent.getBroadcast(applicationContext, alarm.id, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val intent = Intent(this, DesactivationActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("alarm", alarm)
            intent.putExtra("ALARM", bundle)
            val pendingIntent = PendingIntent.getActivity(this, alarm.id, intent, 0)

            val arrayVibrate : LongArray = if (alarm.vibrate) {
                longArrayOf( 1000, 1000, 1000, 1000, 1000 )
            } else {
                longArrayOf(0L)
            }

            return NotificationCompat.Builder(applicationContext, channelID)
                .setContentTitle("Sleewell")
                .setContentText("It's time to wake up !")
                .setSmallIcon(R.drawable.logo_sleewell)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .addAction(R.drawable.logo_sleewell, "Stop", stopPendingIntent)
                .addAction(R.drawable.logo_sleewell, "Snooze", snoozePendingIntent)
                .setVibrate(arrayVibrate)
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