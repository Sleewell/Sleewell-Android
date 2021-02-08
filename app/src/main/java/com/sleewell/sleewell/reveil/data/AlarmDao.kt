package com.sleewell.sleewell.reveil.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sleewell.sleewell.reveil.data.model.Alarm

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarm: Alarm) : Long
    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteUser(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    suspend fun deleteAllAlarms()

    @Query("SELECT * FROM alarm_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarm_table WHERE id = :idAlarm")
    fun getById(idAlarm: Int): Alarm
}