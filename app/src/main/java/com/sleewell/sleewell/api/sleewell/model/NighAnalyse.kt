package com.sleewell.sleewell.api.sleewell.model

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

data class NighAnalyse(
    val token: String,
    val start: Long,
    val data: Array<AnalyseValue>,
    val end: Long
)