package com.sleewell.sleewell.database.analyse.night.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.sleewell.sleewell.database.analyse.night.entities.Analyse
import com.sleewell.sleewell.database.analyse.night.entities.Night

data class NightAndAnalyse(
    @Embedded val night: Night,
    @Relation(
        parentColumn = "uId",
        entityColumn = "nightId"
    )
    val analyses : List<Analyse>
)
