package com.sleewell.sleewell.modules.audio.upload

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IStatsApi
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.api.sleewell.model.PostResponse
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.modules.audio.service.AnalyseServiceTracker
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Class that check if stats are upload and if not upload them
 *
 * @property context
 * @author Hugo Berthomé
 */
class AudioAnalyseUpload(val context: Context) : IAudioAnalyseRecordListener {

    private var scopeIOThread = CoroutineScope(Job() + Dispatchers.IO)

    // Database
    private val analyse : IAnalyseDataManager = AudioAnalyseDbUtils(context, this)

    // API
    private var token = MainActivity.accessTokenSleewell
    private val api: IStatsApi = ApiClient.retrofit.create(IStatsApi::class.java)

    // Filter datas
    private val dbNone = 0.0
    private val maxDataPerMin: Int = 1
    private var timestampMin: Long = -1

    /**
     * Check if need to upload and upload
     *
     * @author Hugo Berthomé
     */
    fun updateUpload() {
        token = MainActivity.accessTokenSleewell
        if (token.isEmpty() || AnalyseServiceTracker.getServiceState(context) == AnalyseServiceTracker.ServiceState.STARTED)
            return
        scopeIOThread.launch {
            analyse.getAvailableAnalyse()
        }
    }

    override fun onListAvailableAnalyses(analyses: List<Night>) {
        analyses.forEach {
            analyse.readAnalyse(it.uId)
        }
    }

    /**
     * Function called when an analyse is read from a file
     *
     * @param data
     * @param nightId
     * @author Hugo berthomé
     */
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>, nightId: Long) {
        if (data.isEmpty())
            return
        if (data.size <= 2) {
            analyse.deleteAnalyse(nightId)
            return
        }
        val dataToSend = filterData(data)
        val toSend =
            NightAnalyse(
                data = dataToSend,
                start = data.first().ts,
                end = data.last().ts,
                id = null
            )
        uploadData(toSend, nightId)
    }

    /**
     * Filter the data to upload and add create data if doesn't exit
     *
     * @param datas
     * @return data to send
     * @author Hugo Berthomé
     */
    private fun filterData(datas: Array<AnalyseValue>): Array<AnalyseValue> {
        if (datas.isEmpty()) {
            return datas
        }

        val queueData = PriorityQueue<AnalyseValue>(
            maxDataPerMin
        ) { o1, o2 -> (o1!!.db - o2!!.db).toInt() }
        var lastData: AnalyseValue? = null
        var nextData: AnalyseValue? = null

        val listData = arrayListOf(
            AnalyseValue(
                dbNone,
                datas.first().ts
            )
        )

        initQueueFromTimestamp(datas[0].ts, queueData, lastData, nextData)
        datas.forEachIndexed { _, analyseValue ->
            nextData = analyseValue
            while (!checkDataTimestampLimit(analyseValue)) {
                popQueueInList(queueData, listData, lastData, nextData)
            }
            lastData = analyseValue
            addInPriorityQueue(analyseValue, queueData)
        }
        popQueueInList(queueData, listData, lastData, nextData)

        listData.add(
            AnalyseValue(
                dbNone,
                listData[listData.size - 1].ts
            )
        )
        queueData.clear()
        lastData = null
        nextData = null
        timestampMin = -1
        return listData.toTypedArray()
    }

    /**
     * Init the priority queue of data to send
     *
     * @param ts
     * @param queueData
     * @param lastData
     * @param nextData
     * @author Hugo Berthomé
     */
    private fun initQueueFromTimestamp(
        ts: Long,
        queueData: PriorityQueue<AnalyseValue>,
        lastData: AnalyseValue?,
        nextData: AnalyseValue?
    ) {
        val offset: Int = 60 / maxDataPerMin

        queueData.clear()
        for (i in 0 until maxDataPerMin) {
            val timestampToAdd = ts + (offset * (i + 1))
            if (lastData != null && nextData != null) {
                queueData.add(
                    AnalyseValue(
                        createValue(timestampToAdd, lastData, nextData),
                        timestampToAdd
                    )
                )
            } else {
                queueData.add(AnalyseValue(dbNone, timestampToAdd))
            }
        }
    }

    /**
     * Create a random value from data uploaded
     *
     * @param ts
     * @param lastData
     * @param nextData
     * @return
     * @author Hugo Berthomé
     */
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

    /**
     * Check if a data is in timestamp limit
     *
     * @param value
     * @return
     * @author Hugo Berthomé
     */
    private fun checkDataTimestampLimit(value: AnalyseValue): Boolean {
        if (timestampMin == -1L) {
            timestampMin = value.ts
            return true
        }
        return value.ts - timestampMin <= 60
    }

    /**
     * Add a data in the priority queue
     *
     * @param value
     * @param queueData
     * @author Hugo Berthomé
     */
    private fun addInPriorityQueue(value: AnalyseValue, queueData: PriorityQueue<AnalyseValue>) {
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

    /**
     * pop the queue in the final list
     *
     * @param queue
     * @param datas
     * @param lastData
     * @param nextData
     * @author Hugo Berthomé
     */
    private fun popQueueInList(
        queue: PriorityQueue<AnalyseValue>,
        datas: ArrayList<AnalyseValue>,
        lastData: AnalyseValue?,
        nextData: AnalyseValue?
    ) {
        var lastPopped: AnalyseValue? = null

        queue.sortedBy { value -> value.ts }.forEach { poppedValue ->
            lastPopped = poppedValue
            datas.add(AnalyseValue(poppedValue.db, poppedValue.ts))
        }
        if (lastPopped != null) {
            timestampMin = lastPopped!!.ts
            initQueueFromTimestamp(lastPopped!!.ts, queue, lastData, nextData)
        }
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordError(msg: String) {
        Log.e(this.javaClass.name, msg)
    }

    private fun uploadData(toSend: NightAnalyse, nightId: Long) {
        if (token.isEmpty())
            return
        val call: Call<PostResponse> = api.postNight("Bearer $token", toSend)

        call.enqueue(object : Callback<PostResponse> {

            override fun onResponse(
                call: Call<PostResponse>,
                response: Response<PostResponse>
            ) {
                val responseRes: PostResponse? = response.body()

                if (responseRes == null) {
                    Log.e(this.javaClass.name, "Body null error")
                    Log.e(this.javaClass.name, "Code : " + response.code())
                } else {
                    analyse.deleteAnalyse(nightId)
                    Log.d(this.javaClass.name, "Stats sent and deleted")
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e(this.javaClass.name, t.toString())
            }

        })
    }

    // Not used
    override fun onAnalyseRecordEnd() {
    }
}