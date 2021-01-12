package com.sleewell.sleewell.mvp.menu.statistics.model

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioAnalyser.AudioAnalyseFileUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract

class StatisticsModel(
    context: AppCompatActivity,
    private val listener: StatisticsContract.Model.Listener
) :
    StatisticsContract.Model, IAudioAnalyseRecordListener {

    private val analyse = AudioAnalyseFileUtils(context, this)
    private var analyseFileDate = ""

    /**
     * Fetch the last analyse recorded in the phone
     *
     * @author Hugo Berthomé
     */
    override fun getLastAnalyse() {
        val files = analyse.readDirectory()
        if (files.isEmpty()) {
            listener.onDataAnalyse(arrayOf())
        } else {
            analyseFileDate = files[0].name
            analyse.readAnalyse(files[0])
        }
    }

    /**
     * Function called when the analyse record has stopped
     *
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordEnd() {
    }

    /**
     * Function called when an analyse is read from a file
     *
     * @param data of the analyse file
     * @author Hugo Berthomé
     */
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
        listener.onDataAnalyseDate(analyseFileDate)
        listener.onDataAnalyse(data)
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordError(msg: String) {
        listener.onError("An error occurred while reading the last available analyse")
    }
}