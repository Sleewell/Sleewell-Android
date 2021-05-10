package com.sleewell.sleewell.mvp.menu.statistics

import com.sleewell.sleewell.api.sleewell.model.ListAnalyse
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic
import com.sleewell.sleewell.mvp.menu.statistics.model.dataClass.AnalyseDetail
import java.util.*
import kotlin.collections.ArrayList

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

        /**
         * Fetch the night in the API
         *
         * @param nightDate nighDate format YYYYMMDD, if empty fetch last night
         * @author Hugo Berthomé
         */
        fun getNight(nightDate: Date)

        /**
         * Fetch the week in the API
         *
         * @param weekDate weekDate format YYYYMMDD, if empty fetch last week
         * @author Hugo Berthomé
         */
        fun getWeek(weekDate: Date)

        /**
         * Fetch the month in the API
         *
         * @param monthDate monthDate format YYYYMM, if empty fetch last month
         * @author Hugo Berthomé
         */
        fun getMonth(monthDate: Date)

        /**
         * Fetch the year in the API
         *
         * @param yearDate yearDate format YYYY, if empty fetch last year
         */
        fun getYear(yearDate: Date)

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

        interface OnApiFinishedListener {
            /**
             * Function called after Night received from the API sleewell
             *
             * @param night
             * @author Hugo Berthomé
             */
            fun onNight(night: NightAnalyse)

            /**
             * Function called after List of Analyse received from the API sleewell
             *
             * @param list
             * @author Hugo Berthomé
             */
            fun onWeekAnalyse(list: ListAnalyse)

            /**
             * Function called after List of Analyse received from the API sleewell
             *
             * @param list
             * @author Hugo Berthomé
             */
            fun onMonthAnalyse(list: ListAnalyse)

            /**
             * Function called after List of Analyse received from the API sleewell
             *
             * @param list
             * @author Hugo Berthomé
             */
            fun onYearAnalyse(list: ListAnalyse)

            /**
             * Function called if a error occurred when calling the API
             *
             * @param t
             * @author Hugo Berthomé
             */
            fun onFailure(t: Throwable)
        }
    }

    interface Presenter : BasePresenter, Model.Listener, Model.OnApiFinishedListener {

        /**
         * Refresh the data from the last analyse
         *
         * @author Hugo Berthomé
         */
        fun refreshAnalyse(date: Date)

        /**
         * Get the current state of the analyse
         *
         * @return State
         * @author Hugo Berthomé
         */
        fun getCurrentState() : State

        /**
         * Set the current state of the analyse
         *
         * @param state
         * @author Hugo Berthomé
         */
        fun setCurrentState(state: State)
    }

    interface View : BaseView<Presenter> {

        /**
         * Update the graph for the daily analyse
         *
         * @param data from the night analyse
         * @author Hugo Berthomé
         */
        fun displayAnalyseDay(data: NightAnalyse)

        /**
         * Update the graph for the Weekly analyse
         *
         * @param datas list of data from the nights analyse
         * @author Hugo Berthomé
         */
        fun displayAnalyseWeek(datas: Array<NightAnalyse>)

        /**
         * Update the graph for the Month analyse
         *
         * @param datas list of data from the nights analyse
         * @author Hugo Berthomé
         */
        fun displayAnalyseMonth(datas: Array<NightAnalyse>)

        /**
         * Update the graph for the year analyse
         *
         * @param datas list of data from the nights analyse
         * @author Hugo Berthomé
         */
        fun displayAnalyseYear(datas: Array<NightAnalyse>)

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

        /**
         * Display night analyse advices to the use
         *
         * @param advices List of the advices to display (if list empty ward will be gone)
         * @author Hugo Berthomé
         */
        fun displayAnalyseAdvices(advices: ArrayList<AnalyseDetail>)
    }
}