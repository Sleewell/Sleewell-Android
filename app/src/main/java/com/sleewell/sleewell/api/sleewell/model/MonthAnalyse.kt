package com.sleewell.sleewell.api.sleewell.model

data class MonthAnalyse(
    val average : Long,
    val endpoint : String?,
    val data : Array<WeekAnalyse>?
)