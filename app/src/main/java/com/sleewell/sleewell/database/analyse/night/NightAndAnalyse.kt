package com.sleewell.sleewell.database.analyse.night

import androidx.room.Embedded
import androidx.room.Relation

data class NightAndAnalyse(
    @Embedded val night: Night,
    @Relation(
        parentColumn = "uId",
        entityColumn = "nightId"
    )
    val analyses : List<Analyse>
)
