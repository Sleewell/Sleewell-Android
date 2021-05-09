package com.sleewell.sleewell.reveil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.os.Vibrator
import androidx.core.content.ContextCompat.getSystemService
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

            val alarmUri = Uri.parse(alarm.ringtone)

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(
                AudioManager.STREAM_ALARM,
                1,
                0
            )

            mp = MediaPlayer()
            mp.setAudioStreamType(AudioManager.STREAM_ALARM)
            mp.setDataSource(context, alarmUri)
            mp.isLooping = true
            mp.prepare()
            mp.start()

            if (alarm.vibrate) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val pattern = longArrayOf(0, 500, 1000)
                vibrator.vibrate(pattern, 0)
            }

            //Increase of 1 every second
            val timer = object: CountDownTimer(10000, 2000) {
                override fun onTick(millisUntilFinished: Long) {
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        audioManager.getStreamVolume(AudioManager.STREAM_ALARM) + 1,
                        0
                    )
                }
                override fun onFinish() {}
            }
            timer.start()
        }
    }
}