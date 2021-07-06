package com.sleewell.sleewell.reveil.data.repository

import androidx.lifecycle.LiveData
import com.sleewell.sleewell.reveil.data.AlarmDao
import com.sleewell.sleewell.reveil.data.model.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {
    val readAllData: LiveData<List<Alarm>> = alarmDao.readAllData()

    /**
     * Add an alarm to the database.
     *
     * @param alarm Alarm to add.
     * @return Id of the alarm
     */
    suspend fun addAlarm(alarm: Alarm): Long {
        return alarmDao.addAlarm(alarm)
    }

    /**
     * Update an alarm in the database.
     *
     * @param alarm Alarm to update.
     */
    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    /**
     * Delete an alarm in the database.
     *
     * @param alarm Alarm to delete.
     */
    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    /**
     * Get an alarm with an id in the database.
     *
     * @param id Id of the alarm to return.
     * @return Alarm corresponding to the id.
     */
    fun getById(id: Int) : Alarm {
        return alarmDao.getById(id)
    }
}