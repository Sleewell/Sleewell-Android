package com.sleewell.sleewell.mvp.menu.statistics

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic

/**
 * Interface that defines how the statistic activity will work
 *
 * @author Hugo Berthomé
 */
interface StatisticsContract {
    interface Model {

        /**
         * Fetch the last analyse recorded in the phone
         *
         * @author Hugo Berthomé
         */
        fun getLastAnalyse()

        interface Listener {

            /**
             * Receives the datas from the last analyse
             *
             * @param datas
             * @author Hugo Berthomé
             */
            fun onDataAnalyse(datas: Array<AnalyseValue>)

            /**
             * Receives the date and time of the last analyse
             *
             * @param date
             * @author Hugo Berthomé
             */
            fun onDataAnalyseDate(date: String)

            /**
             * If an error occurs, this function is called
             *
             * @param msg of error
             * @author Hugo Berthomé
             */
            fun onError(msg: String)
        }
    }

    interface Presenter : BasePresenter, Model.Listener {

        /**
         * Refresh the data from the last analyse
         *
         * @author Hugo Berthomé
         */
        fun refreshAnalyse()
    }

    interface View : BaseView<Presenter> {

        /**
         * Update the graph from the analyse data
         *
         * @param datas from the analyse
         * @author Hugo Berthomé
         */
        fun displayAnalyse(datas: Array<AnalyseValueStatistic>) // TODO to delete

        /**
         * Update the graph for the daily analyse
         *
         * TODO params
         * @author Hugo Berthomé
         */
        fun displayAnalyseDay(/*TODO définir les données de l'analyse*/)

        /**
         * Update the graph for the Weekly analyse
         *
         * TODO params
         * @author Hugo Berthomé
         */
        fun displayAnalyseWeek(/*TODO définir les données de l'analyse*/)

        /**
         * Update the graph for the Month analyse
         *
         * TODO params
         * @author Hugo Berthomé
         */
        fun displayAnalyseMonth(/*TODO définir les données de l'analyse*/)

        /**
         * Update the graph for the year analyse
         *
         * TODO params
         * @author Hugo Berthomé
         */
        fun displayAnalyseYear(/*TODO définir les données de l'analyse*/)

        /**
         * Display the date and time of the analyse
         *
         * @param date to display
         * @author Hugo Berthomé
         */
        fun displayAnalyseDate(date: String)

        /**
         * Display a message saying no analyse and an error icon
         *
         * @author Hugo Berthomé
         */
        fun noAnalyseFound()

        /**
         * Display an error message
         *
         * @param msg to display
         * @author Hugo Berthomé
         */
        fun onError(msg: String)

        /**
         * Display the night information
         *
         * @param timeSleeping total sleeping time
         * @param timeGoingToSleep time when going to sleep
         * @param timeWakingUp time when waking up
         * @author Hugo Berthomé
         */
        fun displayNightData(timeSleeping: Long, timeGoingToSleep: Long, timeWakingUp: Long)
    }
}