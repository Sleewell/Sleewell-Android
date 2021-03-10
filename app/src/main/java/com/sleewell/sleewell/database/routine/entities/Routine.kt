package com.sleewell.sleewell.database.routine.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Routine(
    @PrimaryKey(autoGenerate = true) val uId: Long,

    // Color
    var colorRed: Int,
    var colorGreen: Int,
    var colorBlue: Int,

    // Duration
    var useHalo: Boolean,
    val duration: Int,

    // Music
    var useMusic: Boolean,
    var player: String,


    var name: String,

    ) {
    constructor(name: String, colorRed: Int = 0, colorGreen: Int = 0, colorBlue: Int = 0, useHalo: Boolean = false, duration: Int = 0, useMusic: Boolean = false, player: String = "music") :
            this(0, colorRed, colorGreen, colorBlue, useHalo, duration, useMusic, player, name)
}