package com.sleewell.sleewell.reveil.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import java.lang.reflect.Type

@Parcelize
@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Long,
    val activate: Boolean,
    val days: List<Boolean>,
/*    val sound: String,*/
    val vibrate: Boolean,
    val label: String
): Parcelable

class DataConverter {
    @TypeConverter
    fun fromBooleanList(booleans: List<Boolean?>?): String? {
        if (booleans == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
        return gson.toJson(booleans, type)
    }

    @TypeConverter
    fun toBooleanList(booleansString: String?): List<Boolean>? {
        if (booleansString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Boolean?>?>() {}.type
        return gson.fromJson<List<Boolean>>(booleansString, type)
    }
}