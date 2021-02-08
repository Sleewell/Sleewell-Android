package com.sleewell.sleewell.reveil.data.repository

import androidx.lifecycle.LiveData
import com.sleewell.sleewell.reveil.data.AlarmDao
import com.sleewell.sleewell.reveil.data.model.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {
    val readAllData: LiveData<List<Alarm>> = alarmDao.readAllData()

    suspend fun addAlarm(alarm: Alarm): Long {
        return alarmDao.addAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteUser(alarm)
    }

    suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }

    fun getById(id: Int) : Alarm {
        return alarmDao.getById(id)
    }
}