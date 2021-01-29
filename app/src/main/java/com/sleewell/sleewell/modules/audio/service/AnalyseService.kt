package com.sleewell.sleewell.modules.audio.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.R

class AnalyseService : Service() {

    companion object {
        const val STOP = "stopAnalyse"
    }

    private val mBinder = AnalyseServiceBinder()
    private var stopPendingIntent: PendingIntent? = null

    private var isStarted = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startService()
        parseCmd(intent?.action)
        return START_NOT_STICKY
    }

    /**
     * Class that can connect a service to an activity as a binder
     *
     */
    inner class AnalyseServiceBinder() : Binder() {
        val service get() = this@AnalyseService
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("AnalyseService", "Unbind")
        return super.onUnbind(intent)
    }
    // TODO register last data in file
    override fun onDestroy() {
        super.onDestroy()
        Log.d("AnalyseService", "End of service")
    }

    // =============== All analyse cmds =======


    // TODO change ID from start foreground
    private fun startService() {
        if (stopPendingIntent == null) {
            val stopIntent = Intent(this, AnalyseServiceBroadcast::class.java).setAction(STOP)
            stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0)
        }
        val notification = createStatusNotification()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
        } else {
            startForeground(1, notification)
        }
        isStarted = true
    }

    private fun stopService() {
        displayStoppingNotification()
        val endTimer = object : CountDownTimer(200L, 500) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                stopForeground(true)
                isStarted = false
            }
        }
        endTimer.start()
    }

    private fun parseCmd(action: String?) {
        when (action) {
            STOP -> {
                stopService()
            }
        }
    }

    // ================ Notifications =========================

    fun startAnalyse() {
        if (!isStarted) {
            startService()
        }

        // TODO register sound and analyse here
    }

    fun isStarted(): Boolean {
        return isStarted
    }

    private fun createStatusNotification(): Notification {
        return NotificationCompat.Builder(this, resources.getString(R.string.notification_analyse_channel_id))
            .setContentTitle(resources.getString(R.string.analyse_foreground_notification_name))
            .setContentText(resources.getString(R.string.analyse_foreground_notification_content_text))
            .setSmallIcon(R.drawable.logo_sleewell)
            .addAction(R.drawable.ic_close, "Stop", stopPendingIntent)
            .build()
    }

    // TODO ID à changer
    private fun displayNotification() {
        with(NotificationManagerCompat.from(this@AnalyseService)) {
            notify(1, createStatusNotification())
        }
    }

    //TODO ID à changer
    private fun displayNotification(notification: Notification) {
        with(NotificationManagerCompat.from(this@AnalyseService)) {
            notify(1, notification)
        }
    }

    private fun displayStoppingNotification() {
        displayNotification(NotificationCompat.Builder(this, resources.getString(R.string.notification_analyse_channel_id))
            .setContentTitle(resources.getString(R.string.analyse_foreground_notification_name))
            .setContentText(resources.getString(R.string.analyse_foreground_stopping_notification_content_text))
            .setSmallIcon(R.drawable.logo_sleewell)
            .build())
    }
}