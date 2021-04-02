package com.sleewell.sleewell.mvp.menu.statistics.presenter

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.api.sleewell.model.ListAnalyse
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.api.sleewell.model.PostResponse
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.State
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import com.sleewell.sleewell.mvp.menu.statistics.model.StatisticsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.StringBuilder
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsPresenter(context: AppCompatActivity, private val view: StatisticsContract.View) :
    StatisticsContract.Presenter {

    private var currentState: State = State.DAY

    private var waitingQuery: Date? = null
    private var isWaiting = false

    private val scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private val model: StatisticsContract.Model = StatisticsModel(context, this, this)

    private var startAnalyse: Long = 0

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
    }

    /**
     * Refresh the data from the last analyse
     *
     * @author Hugo Berthomé
     */
    override fun refreshAnalyse(date: Date) {
        if (isWaiting) {
            waitingQuery = date
            return
        }

        when (currentState) {
            State.DAY -> {
                model.getNight(date)
            }
            State.WEEK -> {
                model.getWeek(date)
            }
            State.MONTH -> {
                model.getMonth(date)
            }
            State.YEAR -> {
                model.getYear(date)
            }
        }
        isWaiting = true
    }

    /**
     * Get the current state of the analyse
     *
     * @return State
     * @author Hugo Berthomé
     */
    override fun getCurrentState(): State {
        return currentState
    }

    /**
     * Set the current state of the analyse
     *
     * @param state
     * @author Hugo Berthomé
     */
    override fun setCurrentState(state: State) {
        currentState = state
    }

    /**
     * Receives the datas from the last analyse on the phone
     *
     * @param datas
     * @author Hugo Berthomé
     */
    override fun onDataAnalyse(datas: Array<AnalyseValue>) {
       /* scopeDefault.run {
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
        }*/
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

    /**
     * Function called after Night received from the API sleewell
     *
     * @param night
     * @author Hugo Berthomé
     */
    override fun onNight(night: NightAnalyse) {
        if (checkIfWaiting())
            return
        // TODO display analyse
    }

    /**
     * Function called after List of Analyse received from the API sleewell
     *
     * @param list
     * @author Hugo Berthomé
     */
    override fun onWeekAnalyse(list: ListAnalyse) {
        if (checkIfWaiting())
            return
        TODO("Not yet implemented")
    }

    /**
     * Function called after List of Analyse received from the API sleewell
     *
     * @param list
     * @author Hugo Berthomé
     */
    override fun onMonthAnalyse(list: ListAnalyse) {
        if (checkIfWaiting())
            return
        TODO("Not yet implemented")
    }

    /**
     * Function called after List of Analyse received from the API sleewell
     *
     * @param list
     * @author Hugo Berthomé
     */
    override fun onYearAnalyse(list: ListAnalyse) {
        if (checkIfWaiting())
            return
        TODO("Not yet implemented")
    }

    /**
     * Function called after a Night has been successfully posted
     *
     * @param res
     * @author Hugo Berthomé
     */
    override fun onPostRes(res: PostResponse) {
        TODO("Not yet implemented")
    }

    /**
     * Function called if a error occurred when calling the API
     *
     * @param t
     * @author Hugo Berthomé
     */
    override fun onFailure(t: Throwable) {
        Log.e(this.javaClass.name, t.message.toString())
        if (checkIfWaiting())
            return
        view.onError("An error occurred while fetching analyse")
    }

    private fun timestampToDateString(timestamp: Long, dateFormat: String = "dd / MM"): String {
        return DateTimeFormatter
            .ofPattern(dateFormat)
            .withZone(ZoneOffset.systemDefault())
            .format(Instant.ofEpochSecond(timestamp))
    }

    private fun checkIfWaiting() : Boolean {
        isWaiting = false
        if (waitingQuery != null) {
            waitingQuery?.let {
                refreshAnalyse(it)
                waitingQuery = null
            }
            return true
        }
        return false
    }

    // TODO vvvvvvvv a déplacer lors de l'enregistrement de l'analyse vvvvvv

    /*
    private val maxDataPerMin: Int = 1

    private val dbNone = 0.0
    private val queueData = PriorityQueue<AnalyseValue>(
        maxDataPerMin
    ) { o1, o2 -> (o1!!.db - o2!!.db).toInt() }

    private var timestampMin: Long = -1
    private val formatter = DateTimeFormatter
        .ofPattern("HH:mm")
        .withZone(ZoneOffset.systemDefault())

    private var lastData: AnalyseValue? = null
    private var nextData: AnalyseValue? = null

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
                queueData.add(
                    AnalyseValue(
                        createValue(timestampToAdd, lastData!!, nextData!!),
                        timestampToAdd
                    )
                )
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
    }*/
}