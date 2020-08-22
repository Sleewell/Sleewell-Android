package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager

/**
 * Alarm Receiver
 *
 */
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        lateinit var mp: MediaPlayer
        fun isMpInitialised() = Companion::mp.isInitialized
    }

    /**
     * When alart receiver receive a signal
     *
     * @param context Context of the application
     * @param intent Intent of the application
     */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = AlarmNotificationHelper(context)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
        var alarmUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION)
            if (alarmUri == null) alarmUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE)
        }
        mp = MediaPlayer.create(context, alarmUri)
        mp.isLooping = true
        mp.start()
    }

}