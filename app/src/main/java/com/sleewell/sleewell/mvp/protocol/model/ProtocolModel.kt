package com.sleewell.sleewell.mvp.protocol.model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.MotionEvent
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.audio.audioAnalyser.AudioAnalyser
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audio.audioRecord.RawRecorderManager
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.audioTransformation.Spectrogram
import com.sleewell.sleewell.mvp.protocol.ProtocolContract

/**
 * this class is the model of the halo
 *
 * @param context current context of the app
 * @author gabin warnier de wailly
 */
class ProtocolModel(
    private val audioListener: IRecorderListener,
    private val spectrogramListener: ISpectrogramListener,
    private val context: AppCompatActivity
) : ProtocolContract.Model, IAudioAnalyseListener {
    private var size: Int = 10
    private lateinit var bitmap: Bitmap
    private lateinit var color: ColorFilter

    // Audio analyser
    private val samplingRate = 44100
    private val recorder: IRecorderManager = RawRecorderManager(context, audioListener, samplingRate)
    private val spectrogram = Spectrogram(spectrogramListener, samplingRate)
    private val analyser = AudioAnalyser(context, this, samplingRate)

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
        dialog.setCanceledOnTouchOutside(true);
        return dialog
    }

    /**
     * Record the audio from the mic source
     *
     * @param state
     * @author Hugo Berthomé
     */
    override fun onRecordAudio(state: Boolean) {
        if (!recorder.permissionGranted()) {
            recorder.askPermission()
            if (!recorder.permissionGranted()) {
                audioListener.onAudioError("Permission not granted to record audio, check you phone parameters")
            }
        }
        if (recorder.permissionGranted()) {
            recorder.onRecord(state)
        }
    }

    /**
     * Return if the smartphone is recording
     *
     * @return True if recording, False otherwise
     * @author Hugo Berthomé
     */
    override fun isRecording(): Boolean {
        return recorder.isRecording()
    }

    /**
     * Convert an audio pcm buffer to spectrogram equivalent
     *
     * @param pcmAudio
     * @author Hugo Berthomé
     */
    override fun convertToSpectrogram(pcmAudio: ShortArray) {
        spectrogram.convertToSpectrogramAsync(pcmAudio)
    }

    /**
     * Analyse audio and Save the results
     *
     * @author Hugo Berthomé
     */
    override fun analyseAndSave(spectrogram: Array<DoubleArray>) {
        analyser.addSpectrogramDatas(spectrogram)
    }

    /**
     * Clean up all the resources
     *
     * @author Hugo Berthomé
     */
    override fun cleanUp() {
        recorder.onRecord(false)
        spectrogram.cleanUp()
        analyser.cleanUp()
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onError(msg : String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Function called when the analyse is stopped
     *
     * @author Hugo Berthomé
     */
    override fun onFinish() {
        // Do nothing but is existing if necessary
    }

    /**
     * Function called to receive the result of the analyse
     *
     * @param data
     * @author Hugo Berthomé
     */
    override fun onDataAnalysed(data: AnalyseValue) {
        // Do nothing but is existing if necessary
    }
}