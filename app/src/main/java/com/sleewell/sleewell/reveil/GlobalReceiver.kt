package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.nav.alarms.AlarmsFragment

/**
 * Notification receiver
 *
 */
class GlobalReceiver : BroadcastReceiver() {

    /**
     * Stop the alarm when button 'stop" have been click on the notificatop,
     *
     * @param context Context of the application
     */
    private fun stopAlarm(context: Context?) {
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(1)
        notificationManager.cancelAll()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action != null) {
            when (intent.action) {
                "Stop" -> {
                    AlarmsFragment.instance.cancelAlarm()
                    stopAlarm(context)
                }
                "Snooze" -> {
                    AlarmsFragment.instance.cancelAlarm()
                    AlarmsFragment.instance.snoozeAlarm()
                    stopAlarm(context)
                }
            }
        }
    }
}