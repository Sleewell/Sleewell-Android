package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.CountDownTimer


/**
 * Receiver of the alarm to start the notification
 *
 * @author Romane Bézier
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
     * @author Romane Bézier
     */
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = AlarmNotificationHelper(context)
        val nb = notificationHelper.channelNotification
        notificationHelper.manager?.notify(1, nb.build())
        var alarmUri = RingtoneManager.getActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_ALARM
        )
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_NOTIFICATION
            )
            if (alarmUri == null) alarmUri = RingtoneManager.getActualDefaultRingtoneUri(
                context,
                RingtoneManager.TYPE_RINGTONE
            )
        }
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            10,
            0
        )
        mp = MediaPlayer.create(context, alarmUri)
        mp.isLooping = true
        mp.start()

        //Increase of 1 every 15 seconds during 5 minutes
        val timer = object: CountDownTimer(300000, 15000) {
            override fun onTick(millisUntilFinished: Long) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
                    0
                )
            }
            override fun onFinish() { }
        }
        timer.start()
    }
}