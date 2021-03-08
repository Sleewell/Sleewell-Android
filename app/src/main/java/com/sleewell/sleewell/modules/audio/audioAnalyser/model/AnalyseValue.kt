package com.sleewell.sleewell.modules.audio.audioAnalyser.model

import androidx.room.ColumnInfo

data class AnalyseValue(
    @ColumnInfo(name = "db") val db: Double,
    @ColumnInfo(name = "ts") val ts: Long
)
