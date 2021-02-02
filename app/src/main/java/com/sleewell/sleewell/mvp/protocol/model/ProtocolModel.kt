package com.sleewell.sleewell.mvp.protocol.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.IBinder
import android.view.MotionEvent
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.service.AnalyseService
import com.sleewell.sleewell.modules.audio.service.AnalyseServiceBroadcast
import com.sleewell.sleewell.modules.audio.service.AnalyseServiceTracker
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract

/**
 * this class is the model of the halo
 *
 * @param context current context of the app
 * @author gabin warnier de wailly
 */
class ProtocolModel(
    private val context: AppCompatActivity
) : ProtocolMenuContract.Model, ProtocolContract.Model {
    private var size: Int = 10
    private lateinit var bitmap: Bitmap
    private lateinit var color: ColorFilter

    //Music
    private var mediaPlayer: MediaPlayer? = null

    override fun getSizeOfCircle(): Int {
        return size
    }

    override fun getColorOfCircle(): ColorFilter {
        return color
    }

    override fun upgradeSizeOfCircle() {
        if (size < 1000)
            size += 3
    }

    override fun degradesSizeOfCircle() {
        if (size > 10 && size - 2 > 10)
            size -= 2
    }

    override fun resetSizeOfCircle() {
        size = 10
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun openColorPicker(): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.colorpicker)

        val colorImage = dialog.findViewById(R.id.colorImage) as ImageView
        val resultColor = dialog.findViewById(R.id.resultColor) as ImageView

        resultColor.setColorFilter(Color.rgb(0, 0, 255))
        resultColor.setBackgroundColor(Color.rgb(0, 0, 255))
        this.color = resultColor.colorFilter

        colorImage.isDrawingCacheEnabled = true
        colorImage.buildDrawingCache(true)

        colorImage.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                this.bitmap = colorImage.drawingCache
                if (event.y.toInt() < bitmap.height && event.x.toInt() < bitmap.width && event.y.toInt() > 0 && event.x.toInt() > 0) {
                    val pixel = bitmap.getPixel(event.x.toInt(), event.y.toInt())
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)
                    resultColor.setColorFilter(Color.rgb(r, g, b))
                    resultColor.setBackgroundColor(Color.rgb(r, g, b))
                    this.color = resultColor.colorFilter
                }
            }
            true
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    /**
     * Record the audio from the mic source
     *
     * @param state
     * @author Hugo Berthomé
     */
    override fun onRecordAudio(state: Boolean) {
        if (state) {
            startForeground()
        } else {
            stopForeground()
        }
    }

    /**
     * Return if the smartphone is recording
     *
     * @return True if recording, False otherwise
     * @author Hugo Berthomé
     */
    override fun isRecording(): Boolean {
        return AnalyseServiceTracker.getServiceState(context) == AnalyseServiceTracker.ServiceState.STARTED
    }

    override fun startMusique(name: String) {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        val song = context.resources.getIdentifier(name, "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, song)
        mediaPlayer!!.start()
    }

    override fun pauseMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    override fun resumeMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
        }
    }

    /**
     * Method to cal at the end of the view
     *
     */
    override fun onDestroy() {
    }

    override fun stopMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }

    /**
     * Start the foreground service and the analyse
     *
     * @author Hugo Berthomé
     */
    private fun startForeground() {
        Intent(context, AnalyseService::class.java).also {
            it.action = AnalyseService.START
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                this.context.startForegroundService(it)
            } else {
                this.context.startService(it)
            }
        }
    }

    /**
     * Stop the analyse and the foreground service
     *
     * @author Hugo Berthomé
     */
    private fun stopForeground() {
        if (AnalyseServiceTracker.getServiceState(context) != AnalyseServiceTracker.ServiceState.STARTED)
            return
        with(Intent(context, AnalyseService::class.java)) {
            action = AnalyseService.STOP
            context.startService(this)
        }
    }
}