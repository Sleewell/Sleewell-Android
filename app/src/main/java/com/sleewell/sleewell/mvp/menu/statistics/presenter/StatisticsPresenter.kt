package com.sleewell.sleewell.mvp.menu.statistics.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.model.StatisticsModel

class StatisticsPresenter(context: AppCompatActivity, private val view: StatisticsContract.View) :
    StatisticsContract.Presenter {

    private val model: StatisticsContract.Model = StatisticsModel(context, this)

    override fun refreshAnalyse() {
        model.getLastAnalyse()
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
    }

    override fun onDataAnalyse(datas: Array<AnalyseValue>) {
        if (datas.isEmpty())
            view.noAnalyseFound()
        else
            view.displayAnalyse(datas)
    }

    override fun onError(msg: String) {
        view.noAnalyseFound()
        view.onError(msg)
    }
}