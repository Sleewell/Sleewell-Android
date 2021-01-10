package com.sleewell.sleewell.mvp.menu.statistics

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface StatisticsContract {
    interface Model {
        fun getLastAnalyse()

        interface Listener {
            fun onDataAnalyse(datas : Array<AnalyseValue>)

            fun onError(msg : String)
        }
    }

    interface Presenter : BasePresenter, Model.Listener {
        fun refreshAnalyse()
    }

    interface View : BaseView<Presenter> {
        fun displayAnalyse(datas : Array<AnalyseValue>)

        fun noAnalyseFound()

        fun onError(msg: String)
    }
}