package com.sleewell.sleewell.database.analyse.night.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class NightUpdate(
    @ColumnInfo(name = "uId") val uId: Long,
    @ColumnInfo(name = "end") val stop: Long
)
