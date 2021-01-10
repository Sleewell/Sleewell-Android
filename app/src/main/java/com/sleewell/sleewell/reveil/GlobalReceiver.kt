package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.nav.alarms.AlarmsFragment
import com.sleewell.sleewell.reveil.data.model.Alarm

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
    private fun stopNotification(context: Context?, id: Int) {
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(id)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action != null) {
            val alarm: Alarm
            val bundle = intent.getBundleExtra("ALARM")
            if (bundle != null) {
                alarm = bundle.getParcelable("alarm")!!
                when (intent.action) {
                    "Stop" -> {
                        AlarmsFragment.instance.stopAlarm(alarm)
                        stopNotification(context, alarm.id)
                    }
                    "Snooze" -> {
                        AlarmsFragment.instance.snoozeAlarm(alarm)
                        stopNotification(context, alarm.id)
                    }
                }
            }
        }
    }
}