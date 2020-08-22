package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

/**
 * Alert Receiver
 *
 */
class AlertReceiver : BroadcastReceiver() {

    companion object {
        lateinit var mp: MediaPlayer
        fun isMpInitialised() = Companion::mp.isInitialized
    }

    /**
     * When alert receiver receive a signal
     *
     * @param context Context of the application
     * @param intent Intent of the application
     */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = AlertNotificationHelper(context)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
    }

}