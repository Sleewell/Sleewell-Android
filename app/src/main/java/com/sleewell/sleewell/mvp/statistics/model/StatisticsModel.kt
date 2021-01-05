package com.sleewell.sleewell.mvp.statistics.model

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderManager
import com.sleewell.sleewell.modules.audio.audioRecord.RawRecorderManager
import com.sleewell.sleewell.mvp.statistics.StatisticsContract

class StatisticsModel(
    private val listener: IRecorderListener,
    private val context: AppCompatActivity
) :
    StatisticsContract.Model {
    private val recorder: IRecorderManager = RawRecorderManager(context, listener)
    private var filePath = "${context.cacheDir?.absolutePath}"

    override fun onRecord(state: Boolean) {
        if (!recorder.permissionGranted()) {
            if (!recorder.permissionGranted()) {
                listener.onAudioError("Permission not granted to record audio, check you phone parameters")
            }
        }
        if (recorder.permissionGranted()) {
            recorder.setOutputFile(filePath, "audioRecordTest")
            recorder.onRecord(state)
        }
    }

    override fun isRecording(): Boolean {
        return recorder.isRecording()
    }
}