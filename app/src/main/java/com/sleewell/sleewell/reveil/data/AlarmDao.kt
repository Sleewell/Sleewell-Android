package com.sleewell.sleewell.reveil.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sleewell.sleewell.reveil.data.model.Alarm

@Dao
interface AlarmDao {

    /**
     * Add an alarm to the database.
     *
     * @param alarm Alarm to add.
     * @return Id of the alarm
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarm: Alarm) : Long

    /**
     * Update an alarm in the database.
     *
     * @param alarm Alarm to update.
     */
    @Update
    suspend fun updateAlarm(alarm: Alarm)

    /**
     * Delete an alarm in the database.
     *
     * @param alarm Alarm to delete.
     */
    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    /**
     * Delete all the alarms of the database.
     */
    @Query("DELETE FROM alarm_table")
    suspend fun deleteAllAlarms()

    /**
     * Read all the data of the database.
     *
     * @return List of the data.
     * @author Romane Bézier
     */
    @Query("SELECT * FROM alarm_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Alarm>>

    /**
     * Get an alarm with an id in the database.
     *
     * @param idAlarm Id of the alarm to return.
     * @return Alarm corresponding to the id.
     */
    @Query("SELECT * FROM alarm_table WHERE id = :idAlarm")
    fun getById(idAlarm: Int): Alarm
}