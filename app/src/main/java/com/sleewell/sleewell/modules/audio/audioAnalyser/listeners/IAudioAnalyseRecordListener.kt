package com.sleewell.sleewell.modules.audio.audioAnalyser.listeners

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

interface IAudioAnalyseRecordListener {
    fun onAnalyseRecordEnd()

    fun onReadAnalyseRecord(data : Array<AnalyseValue>)

    fun onAnalyseRecordError(msg : String)
}