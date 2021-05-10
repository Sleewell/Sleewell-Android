package com.sleewell.sleewell.api.sleewell.model

data class YearAnalyse(
    val average : Long,
    val data : Array<MonthAnalyse>
)
