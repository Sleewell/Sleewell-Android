package com.sleewell.sleewell.database.analyse.night

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Night(
    @PrimaryKey(autoGenerate = true) val uId: Long,
    @ColumnInfo(name = "start") val start: Long?,
    @ColumnInfo(name = "end") val end: Long?,
) {
    constructor(start: Long? = null, end: Long? = null) : this(0, start = start, end = end)
}
