package com.sleewell.sleewell.mvp.menu.statistics.model

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseFileUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract

class StatisticsModel(
    context: AppCompatActivity,
    private val listener: StatisticsContract.Model.Listener
) :
    StatisticsContract.Model, IAudioAnalyseRecordListener {

    private val analyse : IAnalyseDataManager = AudioAnalyseDbUtils(context, this)
    private var analyseFileDate = ""

    /**
     * Fetch the last analyse recorded in the phone
     *
     * @author Hugo Berthomé
     */
    override fun getLastAnalyse() {
        analyse.getAvailableAnalyse()
    }

    override fun onListAvailableAnalyses(analyses: List<Long>) {
        if (analyses.isEmpty()) {
            listener.onDataAnalyse(arrayOf())
        } else {
            analyseFileDate = AudioAnalyseFileUtils.timestampToDateString(analyses[analyses.size - 1])
            analyse.readAnalyse(analyses[analyses.size - 1])
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