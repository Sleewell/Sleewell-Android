package com.example.sleewell.nav.alarms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is alarms Fragment"
    }
    val text: LiveData<String> = _text
}