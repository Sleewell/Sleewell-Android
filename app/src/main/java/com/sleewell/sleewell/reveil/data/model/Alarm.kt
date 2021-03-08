package com.sleewell.sleewell.reveil.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Long,
    val activate: Boolean,
/*    val days: List<String>,
    val sound: String,*/
    val vibrate: Boolean,
    val label: String
): Parcelable