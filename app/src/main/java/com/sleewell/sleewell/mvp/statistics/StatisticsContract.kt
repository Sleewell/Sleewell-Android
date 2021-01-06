package com.sleewell.sleewell.mvp.statistics

import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface StatisticsContract {
    interface Model {
        fun onRecord(state: Boolean)
        fun isRecording() : Boolean
    }

    interface Presenter : BasePresenter, IRecorderListener {
        fun onStartClick()
    }

    interface View : BaseView<Presenter> {
        fun displayToast(message: String)
        fun updateGraphAmplitude(array: ShortArray)
        fun updateGraphSpec(magnitude: DoubleArray)
    }
}