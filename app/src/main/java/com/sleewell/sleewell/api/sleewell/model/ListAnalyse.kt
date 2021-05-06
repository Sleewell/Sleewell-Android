package com.sleewell.sleewell.api.sleewell.model

data class ListAnalyse(
    val Error: String? = null,
    val data : Array<NightAnalyse>?,
    val start: Long,
    val end: Long
)

