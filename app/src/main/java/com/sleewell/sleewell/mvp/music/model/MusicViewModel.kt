package com.sleewell.sleewell.mvp.music.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is music Fragment"
    }
    val text: LiveData<String> = _text
}