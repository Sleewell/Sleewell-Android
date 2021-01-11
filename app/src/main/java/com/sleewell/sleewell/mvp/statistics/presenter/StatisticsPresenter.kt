package com.sleewell.sleewell.mvp.statistics.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.audioTransformation.Spectrogram
import com.sleewell.sleewell.mvp.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.statistics.model.StatisticsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

const val LOG_TAG = "StatisticsPresenter"

class StatisticsPresenter(
    private var view: StatisticsContract.View,
    private var context: AppCompatActivity
) : StatisticsContract.Presenter, ISpectrogramListener {
    private val model: StatisticsContract.Model = StatisticsModel(this, context)
    private val spec = Spectrogram(this, 44100)
    private val directoryPath = "${context.cacheDir?.absolutePath}"
    private val fileName = "specFile.txt"
    private val file = File("$directoryPath/$fileName")
    private val outputStream = FileOutputStream(file)
    private var line = 0

    // Coroutine managing
    private var job: Job = Job()
    private var scopeIO = CoroutineScope(job + Dispatchers.IO)

    private val listWindows: Queue<Array<DoubleArray>> = LinkedList<Array<DoubleArray>>()

    override fun onStartClick() {
        model.onRecord(!model.isRecording())
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    /**
     * When a buffer is filled, it will be sent to this callback
     *
     * @param buffer with audio data inside
     * @author Hugo Berthomé
     */
    override fun onAudio(buffer: ShortArray) {
        this.view.updateGraphAmplitude(buffer)

        spec.convertToSpectrogramAsync(buffer)
    }

    override fun onBufferReceived(spectrogram: Array<DoubleArray>) {
        listWindows.add(spectrogram)
    }

    override fun onErrorSpec(msg: String) {
        view.displayToast(msg)
    }

    /**
     * If an error occurred, a message will be sent
     * The record will be stopped
     *
     * @param message - error message
     * @author Hugo Berthomé
     */
    override fun onAudioError(message: String) {
        view.displayToast(message)
    }

    /**
     * On finished event is called when the recording is stopped
     * (not called when an error occurred but onError instead)
     *
     * @author Hugo Berthomé
     */
    override fun onAudioFinished() {
        view.displayToast("Thread finished after 2000 milliseconds")

        scopeIO.launch {
            while (listWindows.size != 0) {
                val spectrogram = listWindows.poll()
                spectrogram?.forEach {
                    if (line != 0) {
                        outputStream.write("\n".toByteArray())
                    }

                    var index = 0
                    var stringToWrite = ""

                    it.forEach { itDouble ->
                        if (index != 0) {
                            stringToWrite += " "
                        }
                        stringToWrite += itDouble.toString()
                        index += 1
                    }
                    outputStream.write(stringToWrite.toByteArray())
                    line += 1
                }
            }
        }
    }
}