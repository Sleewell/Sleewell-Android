package com.sleewell.sleewell.mvp.menu.statistics.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.model.StatisticsModel
import com.sleewell.sleewell.mvp.menu.statistics.model.AnalyseValueStatistic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class StatisticsPresenter(context: AppCompatActivity, private val view: StatisticsContract.View) :
    StatisticsContract.Presenter {

    private val scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private var scopeMainThread = CoroutineScope(Job() + Dispatchers.Main)
    private val model: StatisticsContract.Model = StatisticsModel(context, this)

    private var startAnalyse: Long = 0

    /**
     * Refresh the data from the last analyse
     *
     * @author Hugo Berthomé
     */
    override fun refreshAnalyse() {
        model.getLastAnalyse()
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
    }

    /**
     * Receives the datas from the last analyse
     *
     * @param datas
     * @author Hugo Berthomé
     */
    override fun onDataAnalyse(datas: Array<AnalyseValue>) {
        scopeDefault.run {
            if (datas.isEmpty()) {
                view.noAnalyseFound()
                return
            }

            val formatter = DateTimeFormatter
                .ofPattern("HH:mm")
                .withZone(ZoneOffset.systemDefault())

            var lastData = AnalyseValue(
                0.0,
                startAnalyse
            )

            val listData = mutableListOf(
                AnalyseValueStatistic(
                    0.0,
                    formatter.format(Instant.ofEpochSecond(datas.first().ts))
                )
            )

            datas.forEach {
                if (it.ts == lastData.ts) {
                    if (it.db > lastData.db) {
                        lastData = it
                    }
                } else {

                    while (it.ts - 1 != lastData.ts) {
                        listData.add(
                            AnalyseValueStatistic(
                                lastData.db,
                                formatter.format(Instant.ofEpochSecond(lastData.ts))
                            )
                        )
                        lastData = AnalyseValue(0.0, lastData.ts + 1)
                    }

                    listData.add(
                        AnalyseValueStatistic(
                            lastData.db,
                            formatter.format(Instant.ofEpochSecond(lastData.ts))
                        )
                    )
                    lastData = it
                }
            }
            listData.add(
                AnalyseValueStatistic(
                    lastData.db,
                    formatter.format(Instant.ofEpochSecond(lastData.ts))
                )
            )
            listData.add(
                AnalyseValueStatistic(
                    0.0,
                    formatter.format(Instant.ofEpochSecond(lastData.ts))
                )
            )
            view.displayAnalyse(listData.toTypedArray())
        }
    }

    /**
     * Receives the date and time of the last analyse
     *
     * @param date
     * @author Hugo Berthomé
     */
    override fun onDataAnalyseDate(date: String) {
        val currentFileDate = date.replace(".json", "")
        startAnalyse = LocalDateTime.parse(
            currentFileDate,
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        ).toEpochSecond(OffsetDateTime.now().offset)
        view.displayAnalyseDate(currentFileDate.replace("_", "  "))
    }

    /**
     * If an error occurs, this function is called
     *
     * @param msg of error
     * @author Hugo Berthomé
     */
    override fun onError(msg: String) {
        view.noAnalyseFound()
        view.onError(msg)
    }
}