package com.sleewell.sleewell.modules.audio.audioAnalyser.listeners

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

interface IAudioAnalyseListener {
    fun onError(msg : String)

    fun onFinish()

    fun onDataAnalysed(data : AnalyseValue)
}