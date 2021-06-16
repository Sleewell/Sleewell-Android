package com.sleewell.sleewell.reveil.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sleewell.sleewell.reveil.data.AlarmDatabase
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Alarm>>
    private val repository: AlarmRepository

    init {
        val alarmDao = AlarmDatabase.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        readAllData = repository.readAllData
    }

    /**
     * Add an alarm to the database.
     *
     * @param alarm Alarm to add.
     * @return Id of the alarm
     */
    fun addAlarm(alarm: Alarm): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(repository.addAlarm(alarm))
        }
        return liveData
    }

    /**
     * Update an alarm in the database.
     *
     * @param alarm Alarm to update.
     */
    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarm)
        }
    }

    /**
     * Delete an alarm in the database.
     *
     * @param alarm Alarm to delete.
     */
    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    /**
     * Delete all the alarms of the database.
     */
    fun deleteAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarms()
        }
    }

    /**
     * Get an alarm with an id in the database.
     *
     * @param idAlarm Id of the alarm to return.
     * @return Alarm corresponding to the id.
     */
    fun getById(id: Int) : LiveData<Alarm> {
        val liveData = MutableLiveData<Alarm>()
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(repository.getById(id))
        }
        return liveData
    }
}