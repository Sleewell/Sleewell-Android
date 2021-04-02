package com.sleewell.sleewell.api.sleewell.model

data class ListAnalyse(
    val data : Array<NightAnalyse>?,
    val start: Long,
    val end: Long
)

