package com.sleewell.sleewell.mvp.menu.statistics

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.statistics.model.AnalyseValueStatistic

interface StatisticsContract {
    interface Model {
        fun getLastAnalyse()

        interface Listener {
            fun onDataAnalyse(datas: Array<AnalyseValue>)

            fun onDataAnalyseDate(date: String)

            fun onError(msg: String)
        }
    }

    interface Presenter : BasePresenter, Model.Listener {
        fun refreshAnalyse()
    }

    interface View : BaseView<Presenter> {

        fun displayAnalyse(datas: Array<AnalyseValueStatistic>)
        
        fun displayAnalyseDate(data: String)

        fun noAnalyseFound()

        fun onError(msg: String)
    }
}