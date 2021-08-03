package com.sleewell.sleewell.database.analyse.night.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Night(
    @PrimaryKey(autoGenerate = true) val uId: Long,
    @ColumnInfo(name = "start") val start: Long?,
    @ColumnInfo(name = "end") val end: Long?,
    @ColumnInfo(name = "date") val date: String?,
) {
    constructor(start: Long? = null, end: Long? = null, date: String? = null) : this(0, start = start, end = end, date = date)
}
