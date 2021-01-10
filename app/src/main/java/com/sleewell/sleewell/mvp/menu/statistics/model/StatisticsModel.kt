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

    override fun getLastAnalyse() {
        val files = analyse.readDirectory()
        if (files.isEmpty()) {
            listener.onDataAnalyse(Array(0) { AnalyseValue() })
        } else {
            analyse.readAnalyse(files[0])
        }
    }

    override fun onAnalyseRecordEnd() {
    }

    override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
        // TODO( "modifier l'array pour supprimer les données avec le meme timestamp qui ont les DB les plus faibles" )
        listener.onDataAnalyse(data)
    }

    override fun onAnalyseRecordError(msg: String) {
        listener.onError("An error occurred while reading the last available analyse")
    }
}