package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.mvp.menu.alarm.view.AlarmFragment

/**
 * Notification receiver
 *
 * @author Romane Bézier
 */
class GlobalReceiver : BroadcastReceiver() {

    /**
     * Stop the alarm when button 'stop" have been click on the notificatop,
     *
     * @param context Context of the application
     * @author Romane Bézier
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
                    AlarmFragment.instance.cancelAlarm()
                    stopAlarm(context)
                }
                "Snooze" -> {
                    AlarmFragment.instance.cancelAlarm()
                    AlarmFragment.instance.snoozeAlarm()
                    stopAlarm(context)
                }
            }
        }
    }
}