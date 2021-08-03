package com.sleewell.sleewell.database.routine.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Routine(
    @PrimaryKey(autoGenerate = true) val uId: Long,

    // ApiId
    var apiId: Int,

    // Selected
    var isSelected: Boolean,

    // Color
    var colorRed: Int,
    var colorGreen: Int,
    var colorBlue: Int,

    // Duration
    var useHalo: Boolean,
    var duration: Int,

    // Music
    var useMusic: Boolean,
    var player: String,
    var musicName: String,
    var musicUri: String,

    // Name
    var name: String,

    // State
    var state: Int,

    ) {
    constructor(name: String, isSelected: Boolean = false, apiId: Int = -1, colorRed: Int = 0, colorGreen: Int = 0, colorBlue: Int = 0, useHalo: Boolean = false, duration: Int = 0, useMusic: Boolean = false, player: String = "music", musicName: String = "", musicUri: String = "", state: Int = RoutineState.NONE.ordinal) :
            this(0, apiId, isSelected, colorRed, colorGreen, colorBlue, useHalo, duration, useMusic, player, musicName, musicUri, name, state)
}