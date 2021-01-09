package com.sleewell.sleewell.mvp.menu.stat.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is statistics Fragment"
    }
    val text: LiveData<String> = _text
}