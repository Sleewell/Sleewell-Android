package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sleewell.sleewell.reveil.data.model.Alarm

/**
 * Receiver of the alert.
 *
 * @author Romane Bézier
 */
class AlertReceiver : BroadcastReceiver() {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
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