package com.sleewell.sleewell.api.sleewell.model

data class WeekAnalyse(
    val average : Long,
    val endpoint : String?,
    val data : Array<NighAnalyse>?
)

