package com.sleewell.sleewell.database.analyse.night

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Analyse(
    @PrimaryKey(autoGenerate = true) val uId: Long,
    val nightId: Long,
    val db: Double,
    val ts: Long
) {
    constructor(nightId: Long = 0, db: Double = 0.0, ts: Long = 0) : this(0, nightId, db, ts)
}