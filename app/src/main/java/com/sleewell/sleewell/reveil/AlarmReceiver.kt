package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.CountDownTimer
import com.sleewell.sleewell.reveil.data.model.Alarm

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

            val notificationHelper = AlarmNotificationHelper(context, alarm)

            val nb = notificationHelper.channelNotification
            notificationHelper.manager?.notify(alarm.id, nb.build())
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

            //Increase of 1 every second
            val timer = object: CountDownTimer(300000, 1000) {
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
}