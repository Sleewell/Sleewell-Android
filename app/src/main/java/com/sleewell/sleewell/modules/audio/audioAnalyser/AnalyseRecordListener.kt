package com.sleewell.sleewell.modules.audio.audioAnalyser

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

interface AnalyseRecordListener {
    fun onAnalyseRecordEnd()

    fun onReadAnalyseRecord(data : Array<AnalyseValue>)

    fun onAnalyseRecordError(msg : String)
}