package com.sleewell.sleewell.modules.audio.audioAnalyser

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.modules.time.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import kotlin.math.log

/**
 * Analyse Audio from a spectrogram
 *
 * @author Hugo Berthomé
 */
class AudioAnalyser(
    context: Context,
    private val listener: IAudioAnalyseListener,
    private val samplingRate: Int = 44100
) :
    IAudioAnalyseRecordListener {
    private val classTag = "AUDIO_ANALYSER"

    // Analyse
    private val minHz = 0
    private val maxHz = 10000
    private val dbMinDetection = 20.0

    // file save
    private var isInitialised = false
    private var isInitializing = false
    private val fileUtil : IAnalyseDataManager = AudioAnalyseDbUtils(context, this)

    // coroutine
    private val queueData: Queue<DoubleArray> = LinkedList()
    private val scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private var stopThread = false
    private var isThreadRunning = false

    /**
     * Add a window from spectrogram to analyse
     *
     * @param data window from the spectrogram
     * @author Hugo Berthomé
     */
    fun addSpectrogramData(data: DoubleArray) {
        queueData.add(data)
        launchAnalyse()
    }

    /**
     * Add a spectrogram to analyse
     *
     * @param datas spectrogram
     * @author Hugo Berthomé
     */
    fun addSpectrogramDatas(datas: Array<DoubleArray>) {
        datas.forEach {
            queueData.add(it)
        }
        launchAnalyse()
    }

    /**
     * Launch the analyse of the spectrogram in the queue
     *
     * @author Hugo Berthomé
     */
    private fun launchAnalyse() {
        if (!isInitialised && !isInitializing) {
            isInitializing = true
            fileUtil.getAvailableAnalyse()
            return
        }
        if (isInitializing || !isInitialised || !isThreadRunning) {
            scopeDefault.run {
                analyseQueue()
            }
        }
    }

    /**
     * Analyse the data in the queue
     *
     * @author Hugo Berthomé
     */
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

    /**
     * Analyse a part of the spectrogram
     *
     * @param spectrogram
     * @author Hugo Berthomé
     */
    private fun analyse(spectrogram: DoubleArray) {
        val datas = extractFrequencies(minHz, maxHz, spectrogram)

        val averageDb = ampToDb(datas.maxOrNull())
        if (averageDb >= dbMinDetection) {
            fileUtil.addToAnalyse(AnalyseValue(averageDb, TimeUtils.getCurrentTimestamp()))
        }
    }

    /**
     * Extracts a part of the window of the spectrogram between to frequencies
     *
     * @param minHz
     * @param maxHz
     * @param spectrogram
     * @return doubleArray
     * @author Hugo Berthomé
     */
    private fun extractFrequencies(minHz: Int, maxHz: Int, spectrogram: DoubleArray): DoubleArray {
        val indexMin: Int = minHz * spectrogram.size / (samplingRate / 2)
        val indexMax: Int = maxHz * spectrogram.size / (samplingRate / 2)

        return spectrogram.sliceArray(IntRange(indexMin, indexMax))
    }

    /**
     * Converts an amplitude into a dB
     *
     * @param amp
     * @param ref
     * @return double
     * @author Hugo Berthomé
     */
    private fun ampToDb(amp: Double?, ref: Double = 1.0): Double {
        if (amp == null)
            return 0.0
        return 10 * log((amp / ref), 10.0)
    }

    /**
     * Function to call at the end to be sure that all the thread are stopped !
     *
     * @author Hugo Berthomé
     */
    fun cleanUp() {
        stopThread = true
        isInitialised = false
        fileUtil.endNewAnalyse()
    }

    /**
     * Function called when the analyse record has stopped
     *
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordEnd() {
        listener.onFinish()
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordError(msg: String) {
        Log.e(classTag, "An error occurred while saving analyse")
        listener.onError("An error occurred while saving analyse")
    }

    /**
     * Function called when received the list of available analyse
     *
     * @param analyses
     */
    override fun onListAvailableAnalyses(analyses: List<Night>) {
        scopeDefault.launch {
            analyses.forEach {
                fileUtil.deleteAnalyse(it.uId)
            }
            if (!fileUtil.initNewAnalyse()) {
                listener.onError("Couldn't initialised ")
                return@launch
            }
            isInitialised = true
            isInitializing = false

            if (!isThreadRunning) {
                scopeDefault.run {
                    analyseQueue()
                }
            }

            listener.onDataManagerInitialized()
        }
    }

    /**
     * Function called when an analyse is read from a file
     *
     * @param data
     * @param nightId
     * @author Hugo berthomé
     */
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>, nightId: Long) {
    }
}