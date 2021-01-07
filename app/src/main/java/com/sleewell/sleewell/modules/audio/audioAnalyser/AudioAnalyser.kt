package com.sleewell.sleewell.modules.audio.audioAnalyser

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.math.log

/**
 * Analyse Audio from a spectrogram
 *
 * @author Hugo Berthomé
 */
class AudioAnalyser(
    context: AppCompatActivity,
    private val listener: IAudioAnalyseListener,
    private val samplingRate: Int = 44100
) :
    IAudioAnalyseRecordListener {
    val CLASS_TAG = "AUDIO_ANALYSER"

    // Analyse
    private val minHz = 0
    private val maxHz = 10000
    private val dbMinDetection = 20.0

    // file save
    private var isInitialised = false
    private val fileUtil = AudioAnalyseFileUtils(context, this)

    // coroutine
    private val queueData: Queue<DoubleArray> = LinkedList<DoubleArray>()
    private val scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private var stopThread = false
    private var isThreadRunning = false

    fun addSpectrogramData(data: DoubleArray) {
        queueData.add(data)
        launchAnalyse()
    }

    fun addSpectrogramDatas(datas: Array<DoubleArray>) {
        datas.forEach {
            queueData.add(it)
        }
        launchAnalyse()
    }

    private fun launchAnalyse() {
        if (!isInitialised) {
            fileUtil.deleteAnalyses(fileUtil.readDirectory())
            if (!fileUtil.initSaveNewAnalyse()) {
                listener.onError("Couldn't initialised ")
                return
            }
            isInitialised = true
        }
        if (!isThreadRunning) {
            scopeDefault.run {
                analyseQueue()
            }
        }
    }

    private fun analyseQueue() {
        isThreadRunning = true
        while (queueData.size != 0) {
            if (stopThread)
                break

            val data = queueData.poll() ?: continue
            analyse(data)
        }
        isThreadRunning = false
    }

    private fun analyse(spectrogram: DoubleArray) {
        val datas = extractFrequencies(minHz, maxHz, spectrogram)

        //val averageDb = ampToDb(datas.average())
        val averageDb = ampToDb(datas.maxOrNull())
        if (averageDb >= dbMinDetection) {
            fileUtil.addToAnalyse(AnalyseValue(averageDb, fileUtil.getCurrentTimestamp()))
        }
    }

    private fun extractFrequencies(minHz: Int, maxHz: Int, spectrogram: DoubleArray): DoubleArray {
        val indexMin: Int = minHz * spectrogram.size / (samplingRate / 2)
        val indexMax: Int = maxHz * spectrogram.size / (samplingRate / 2)

        return spectrogram.sliceArray(IntRange(indexMin, indexMax))
    }

    private fun ampToDb(amp: Double?, ref: Double = 1.0): Double {
        if (amp == null)
            return 0.0
        return 10 * log((amp / ref), 10.0)
    }

    fun cleanUp() {
        stopThread = true
        isInitialised = false
        fileUtil.stopSavingNewAnalyse()
    }

    override fun onAnalyseRecordEnd() {
        listener.onFinish()
    }

    override fun onAnalyseRecordError(msg: String) {
        Log.e(CLASS_TAG, "An error occurred while saving analyse")
        listener.onError("An error occurred while saving analyse")
    }

    // do nothing because we only save
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
    }
}