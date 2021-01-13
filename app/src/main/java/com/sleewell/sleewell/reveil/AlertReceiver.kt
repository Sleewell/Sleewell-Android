package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sleewell.sleewell.reveil.data.model.Alarm

/**
 * Receiver of the alarm
 *
 * @author Romane Bézier
 */
class AlertReceiver : BroadcastReceiver() {
    /**
     * When alert receiver receive a signal
     *
     * @param context Context of the application
     * @param intent Intent of the application
     * @author Romane Bézier
     */
    override fun onReceive(context: Context, intent: Intent) {
        val alarm: Alarm

        val bundle = intent.getBundleExtra("ALARM")
        if (bundle != null) {
            alarm = bundle.getParcelable("alarm")!!
            val notificationHelper = AlertNotificationHelper(context, alarm)
            val nb = notificationHelper.channelNotification
            notificationHelper.manager?.notify(alarm.id, nb.build())
        }
    }
}