package com.sleewell.sleewell.mvp.statistics.model

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audioRecord.RawRecorderManager
import com.sleewell.sleewell.mvp.statistics.StatisticsContract

class StatisticsModel(listener: IRecorderListener, context: AppCompatActivity) : StatisticsContract.Model {
    private val recorder: IRecorderManager = RawRecorderManager(context, listener)

    override fun onRecord(state: Boolean) {
        recorder.onRecord(state)
    }

    override fun isRecording(): Boolean {
        return recorder.isRecording()
    }
}