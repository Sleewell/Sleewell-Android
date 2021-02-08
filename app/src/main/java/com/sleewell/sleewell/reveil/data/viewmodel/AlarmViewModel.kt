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

    fun addAlarm(alarm: Alarm): LiveData<Long> {
        val liveData = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(repository.addAlarm(alarm))
        }
        return liveData
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    fun deleteAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarms()
        }
    }

    fun getById(id: Int) : Alarm {
        return repository.getById(id)
    }
}