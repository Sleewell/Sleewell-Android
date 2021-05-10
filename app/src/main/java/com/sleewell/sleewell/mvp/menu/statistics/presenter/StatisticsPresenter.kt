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
import java.util.*

class StatisticsPresenter(context: AppCompatActivity, private val view: StatisticsContract.View) :
    StatisticsContract.Presenter {

    private val scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private val model: StatisticsContract.Model = StatisticsModel(context, this)

    private var startAnalyse: Long = 0
    private val maxDataPerMin: Int = 1
    private val dbNone = 0.0
    private val queueData = PriorityQueue<AnalyseValue>(
        maxDataPerMin
    ) { o1, o2 -> (o1!!.db - o2!!.db).toInt() }

    private var timestampMin: Long = -1
    private val formatter = DateTimeFormatter
        .ofPattern("HH:mm")
        .withZone(ZoneOffset.systemDefault())

    private var lastData : AnalyseValue? = null
    private var nextData : AnalyseValue? = null

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

            val listData = mutableListOf(
                AnalyseValueStatistic(
                    dbNone,
                    formatter.format(Instant.ofEpochSecond(datas.first().ts))
                )
            )

            initQueueFromTimestamp(datas[0].ts)
            datas.forEachIndexed { index, analyseValue ->
                nextData = analyseValue
                while (!checkDataTimestampLimit(analyseValue)) {
                    popQueueInList(queueData, listData)
                }
                lastData = analyseValue
                addInPriorityQueue(analyseValue)
            }
            popQueueInList(queueData, listData)

            listData.add(
                AnalyseValueStatistic(
                    dbNone,
                    listData[listData.size - 1].ts
                )
            )
            view.displayAnalyse(listData.toTypedArray())
        }
    }

    private fun checkDataTimestampLimit(value: AnalyseValue): Boolean {
        if (timestampMin == -1L) {
            timestampMin = value.ts
            return true
        }
        return value.ts - timestampMin <= 60
    }

    private fun addInPriorityQueue(value: AnalyseValue) {
        if (queueData.size >= maxDataPerMin) {
            val dataPolled = queueData.poll()
            if (dataPolled != null) {
                if (dataPolled.db > value.db) {
                    queueData.add(dataPolled)
                } else {
                    queueData.add(value)
                }
                return
            }
        } else {
            queueData.add(value)
        }
        queueData.add(value)
    }

    private fun initQueueFromTimestamp(ts: Long) {
        val offset: Int = 60 / maxDataPerMin

        queueData.clear()
        for (i in 0 until maxDataPerMin) {
            val timestampToAdd = ts + (offset * (i + 1))
            if (lastData != null && nextData != null) {
                queueData.add(AnalyseValue(createValue(timestampToAdd, lastData!!, nextData!!), timestampToAdd))
            } else {
                queueData.add(AnalyseValue(dbNone, timestampToAdd))
            }
        }
    }

    private fun createValue(ts: Long, lastData: AnalyseValue, nextData: AnalyseValue): Double {
        val end = 9.0
        val start = 5.0
        val randomLimit = Math.random() * (end - start + 1) + start

        val middleDistance = (nextData.ts - lastData.ts) / 2
        val middleValue = lastData.ts + middleDistance

        if (middleDistance == 0L || ts < lastData.ts || ts > nextData.ts)
            return randomLimit
        if (ts < middleValue) {
            val offset = (lastData.db - randomLimit) / middleDistance
            return randomLimit + (offset * (middleValue - ts))
        } else if (ts > middleValue) {
            val offset = (nextData.db - randomLimit) / middleDistance
            return randomLimit + (offset * (ts - middleValue))
        }
        return randomLimit
    }

    private fun popQueueInList(
        queue: PriorityQueue<AnalyseValue>,
        datas: MutableList<AnalyseValueStatistic>
    ) {
        var lastPopped: AnalyseValue? = null

        queue.sortedBy { value -> value.ts }.forEach { poppedValue ->
            lastPopped = poppedValue
            datas.add(
                AnalyseValueStatistic(
                    poppedValue.db,
                    formatter.format(Instant.ofEpochSecond(poppedValue.ts))
                )
            )
        }
        if (lastPopped != null) {
            timestampMin = lastPopped!!.ts
            initQueueFromTimestamp(lastPopped!!.ts)
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