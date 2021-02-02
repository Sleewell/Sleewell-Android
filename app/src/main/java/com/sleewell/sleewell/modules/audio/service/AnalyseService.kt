package com.sleewell.sleewell.modules.audio.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioAnalyser.AudioAnalyser
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audio.audioRecord.RawRecorderManager
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.audioTransformation.Spectrogram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Service that record and analyse sound from the night
 *
 * @author Hugo Berthomé
 */
class AnalyseService : Service() {

    companion object {
        const val STOP = "stopAnalyse"
        const val START = "startAnalyse"
    }

    // Service var
    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false

    private var stopPendingIntent: PendingIntent? = null

    // Audio analyser
    private val samplingRate = 44100
    private var isAnalyseRunning = false

    // =============== Service ==============

    override fun onCreate() {
        super.onCreate()
        Log.d("AnalyseService", "Create")
        if (stopPendingIntent == null) {
            val stopIntent = Intent(this, AnalyseServiceBroadcast::class.java).setAction(STOP)
            stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0)
        }
        val notification = createStatusNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.d("AnalyseService", "Start")
        if (intent != null) {
            parseCmd(intent.action)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AnalyseService", "Destroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun parseCmd(action: String?) {
        when (action) {
            START -> {
                startService()
            }
            STOP -> {
                stopService()
            }
        }
    }

    /**
     * Stop the service and the analyse at the same time
     *
     * @author Hugo Berthomé
     */
    private fun stopService() {
        try {
            Log.d("AnalyseService", "Service stop")
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopAnalyse()
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        AnalyseServiceTracker.setServiceState(this, AnalyseServiceTracker.ServiceState.STOPPED)
    }

    /**
     * Start the service and the analyse
     *
     * @author Hugo Berthomé
     */
    private fun startService() {
        if (isServiceStarted) return

        AnalyseServiceTracker.setServiceState(this, AnalyseServiceTracker.ServiceState.STARTED)
        isServiceStarted = true

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        startAnalyse()
    }

    // ================ Analyse =========================

    /**
     * Start the analyse
     *
     * @author Hugo Berthomé
     */
    private fun startAnalyse() {
        if (!isServiceStarted) {
            startService()
        }

        if (!isAnalyseRunning) {
            isAnalyseRunning = true
            GlobalScope.launch(Dispatchers.Default) {
                val analyser = AudioAnalyser(this@AnalyseService, object : IAudioAnalyseListener {
                    override fun onError(msg: String) {
                        Log.e("ServiceAnalyse", "Audio analyser $msg")
                        displayToast(msg)
                        isAnalyseRunning = false
                    }
                    override fun onFinish() {
                        Log.d("ServiceAnalyse", "Analyse finished")
                    }
                    override fun onDataAnalysed(data: AnalyseValue) {
                    }

                }, samplingRate)
                val spectrogram = Spectrogram(object : ISpectrogramListener {
                    override fun onBufferReceived(spectrogram: Array<DoubleArray>) {
                        analyser.addSpectrogramDatas(spectrogram)
                    }

                    override fun onErrorSpec(msg: String) {
                        Log.e("ServiceAnalyse", "Spectrogram $msg")
                        displayToast(msg)
                        isAnalyseRunning = false
                    }
                }, samplingRate)
                val recorder = RawRecorderManager(this@AnalyseService, object : IRecorderListener {
                    override fun onAudio(buffer: ShortArray) {
                        spectrogram.convertToSpectrogramAsync(buffer)
                    }

                    override fun onAudioError(message: String) {
                        Log.e("ServiceAnalyse", "Record : $message")
                        displayToast(message)
                        isAnalyseRunning = false
                    }

                    override fun onAudioFinished() {
                        Log.d("ServiceAnalyse", "Record finished")
                        displayToast("Record stopped")
                    }

                }, samplingRate)

                if (!recorder.permissionGranted()) {
                    isAnalyseRunning = false
                    return@launch
                }

                recorder.onRecord(true)
                while (isAnalyseRunning) {
                    delay(500);
                }
                recorder.onRecord(false)
                spectrogram.cleanUp()
                analyser.cleanUp()
            }
        }
    }

    /**
     * Stop the analyse
     *
     * @author Hugo Berthomé
     */
    private fun stopAnalyse() {
        if (isAnalyseRunning) {
            isAnalyseRunning = false
        }
    }

    /*============ Notifications ============*/

    /**
     * Create the main notification to display as a foreground service
     *
     * @return the nnotification
     */
    private fun createStatusNotification(): Notification {
        val builder: Notification.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                resources.getString(R.string.notification_analyse_channel_id)
            ) else Notification.Builder(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            builder.addAction(
                Notification.Action.Builder(
                    R.drawable.ic_close,
                    "Stop",
                    stopPendingIntent
                ).build()
            )
        else
            builder.addAction(R.drawable.ic_close, "Stop", stopPendingIntent)

        return builder
            .setContentTitle(resources.getString(R.string.analyse_foreground_notification_name))
            .setContentText(resources.getString(R.string.analyse_foreground_notification_content_text))
            .setSmallIcon(R.drawable.ic_sleewell)
            .build()
    }

    /**
     * Display a toast in the main thread
     *
     * @param msg to display
     */
    private fun displayToast(msg: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@AnalyseService, msg, Toast.LENGTH_LONG).show()
        }
    }
}