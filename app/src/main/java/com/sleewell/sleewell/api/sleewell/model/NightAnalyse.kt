package com.sleewell.sleewell.api.sleewell.model

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

data class NightAnalyse(
    val Error: String? = null,
    val start: Long,
    val data: Array<AnalyseValue>?,
    val end: Long,
    val id: String?,
    val fusion: Boolean? = null,
    val date: String? = null
)