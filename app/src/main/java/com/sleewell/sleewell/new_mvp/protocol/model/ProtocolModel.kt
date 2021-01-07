package com.sleewell.sleewell.new_mvp.protocol.model

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.sleewell.sleewell.modules.audio.audioAnalyser.AnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.AudioAnalyseFileUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audio.audioRecord.RawRecorderManager
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.audioTransformation.Spectrogram
import com.sleewell.sleewell.new_mvp.protocol.ProtocolContract

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
) : ProtocolContract.Model, AnalyseRecordListener {

    private var size: Int = 10
    private lateinit var bitmap: Bitmap
    private lateinit var color: ColorFilter

    private val recorder: IRecorderManager = RawRecorderManager(context, audioListener)
    private val spectrogram = Spectrogram(spectrogramListener, 44100)

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
        //TODO("Not yet implemented")
    }

    // TODO remove all the functions bellow maybe, depends how analyse is implemented

    val utils = AudioAnalyseFileUtils(context, this)

    /**
     * Clean up all the resources
     *
     * @author Hugo Berthomé
     */
    override fun cleanUp() {
        recorder.onRecord(false)
        spectrogram.cleanUp()

        utils.initSaveNewAnalyse()
        utils.addToAnalyse(AnalyseValue(20.0, 20))
        utils.addToAnalyse(AnalyseValue(21.0, 22))
        utils.stopSavingNewAnalyse()
    }

    override fun onAnalyseRecordEnd() {
        val test2 = utils.readDirectory()
        test2.forEach {
            val data = utils.readAnalyse(it)
            val data2 = 0
        }
    }

    override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
        val i = 0
    }

    override fun onAnalyseRecordError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}