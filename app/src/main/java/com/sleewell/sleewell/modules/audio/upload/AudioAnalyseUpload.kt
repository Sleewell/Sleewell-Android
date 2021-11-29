package com.sleewell.sleewell.modules.audio.upload

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sleewell.sleewell.R
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
import com.sleewell.sleewell.modules.time.TimeUtils
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

    private val minLength: Int = 60 * 60
    private var scopeIOThread = CoroutineScope(Job() + Dispatchers.IO)
    private var scopeViewThread = CoroutineScope(Job() + Dispatchers.Main)

    // Database
    private val analyse: IAnalyseDataManager = AudioAnalyseDbUtils(context, this)

    // API
    private var TOKEN = MainActivity.accessTokenSleewell
    private val api: IStatsApi = ApiClient.retrofit.create(IStatsApi::class.java)

    // Filter datas
    private val dbNone = 0.0
    private val maxDataPerMin: Int = 1
    private var timestampMin: Long = -1

    // List night to ask fusion
    private val listData: Queue<Array<AnalyseValue>> = LinkedList()
    private val listNightId: Queue<Long> = LinkedList()
    private var lastNightId: Long = 0;

    /**
     * Check if need to upload and upload
     *
     * @author Hugo Berthomé
     */
    fun updateUpload() {
        TOKEN = MainActivity.accessTokenSleewell
        if (TOKEN.isEmpty() || AnalyseServiceTracker.getServiceState(context) == AnalyseServiceTracker.ServiceState.STARTED)
            return
        scopeIOThread.launch {
            analyse.getAvailableAnalyse()
        }
    }

    override fun onListAvailableAnalyses(analyses: List<Night>) {
        if (analyses.isEmpty())
            return

        lastNightId = analyses.last().uId
        for (night in analyses) {
            analyse.readAnalyse(night.uId)
        }
    }

    private fun checkFusionList() {
        if (TOKEN.isEmpty())
            return
        val datas = listData.poll()
        val nightId = listNightId.poll()

        // Ask for existing night
        if (datas.isNullOrEmpty() || nightId == null)
            return
        val dateToAsk = TimeUtils.getNightDateFromTimeStamp(datas.first().ts)
        val call: Call<NightAnalyse> = api.getNight("Bearer $TOKEN", dateToAsk)
        call.enqueue(object : Callback<NightAnalyse> {

            override fun onResponse(
                call: Call<NightAnalyse>,
                response: Response<NightAnalyse>
            ) {
                val responseRes: NightAnalyse? = response.body()

                if (responseRes == null) {
                    // ERROR
                    Log.e(this.javaClass.name, "Body null error")
                    Log.e(this.javaClass.name, "Code : " + response.code())
                } else {
                    // SUCCESS
                    if (responseRes.data.isNullOrEmpty()) {
                        uploadData(datas, nightId, dateToAsk)
                    } else {
                        showDialog(datas, nightId, responseRes, dateToAsk)
                    }
                    Log.d(this.javaClass.name, "Stats found, ask for fusion")
                }
            }

            override fun onFailure(call: Call<NightAnalyse>, t: Throwable) {
                // ERROR
                Log.e(this.javaClass.name, t.toString())
            }

        })
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
            Log.e(
                this.javaClass.name,
                "Night $nightId too small to be sent for Analyse : length ${data.size}"
            )
            analyse.deleteAnalyse(nightId)
            return
        }

        val hours = TimeUtils.getHourFromTimestampFromLocalTimezone(data.first().ts)

        // If data is over 1 days, don't register it or if is not during night, Between 18h and 7h
        if (TimeUtils.getCurrentTimestamp() - data.first().ts >= 60 * 60 * 24 * 1 || (hours in 9..17)) {
            Log.e(
                this.javaClass.name, "Night $nightId too old to register or not a real night"
            )
            analyse.deleteAnalyse(nightId)
            return
        }

        // If data is too small, check if fusion with last night (must be less than 5 hours difference)
        if (data.first().ts - data.last().ts < minLength) {
            listData.add(data)
            listNightId.add(nightId)
        } else {
            val date = TimeUtils.getNightDateFromTimeStamp(data.first().ts)
            uploadData(data, nightId, date)
        }

        if (lastNightId == nightId) {
            scopeViewThread.launch {
                checkFusionList()
            }
        }
    }

    /**
     * Display dialog to fuse or create new night
     *
     * @param datas
     * @param nightId
     * @param nightFromApi
     */
    private fun showDialog(
        datas: Array<AnalyseValue>,
        nightId: Long,
        nightFromApi: NightAnalyse,
        date: String
    ) {
        val year = date.substring(0..3)
        val month = date.substring(4..5)
        val day = date.substring(6..7)

        MaterialAlertDialogBuilder(context)
            .setCancelable(false)
            .setMessage("A night exist for $year-$month-$day, do you want to merge or create a new whole night with the last registered ?")
            .setNegativeButton("New") { dialog, _ ->
                dialog.dismiss()
                scopeIOThread.launch {
                    uploadData(datas, nightId, date)
                }
                checkFusionList()
            }
            .setPositiveButton("Merge") { dialog, _ ->
                dialog.dismiss()
                scopeIOThread.launch {
                    uploadData(datas, nightId, date, true)
                }
                checkFusionList()
            }.show()
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

    // TODO envoyer le paramètre indiquant la fusion et la date de la fusion
    private fun uploadData(
        data: Array<AnalyseValue>,
        nightId: Long,
        date: String,
        fusion: Boolean = false
    ) {
        if (TOKEN.isEmpty())
            return

        val dataToSend = filterData(data)
        val toSend =
            NightAnalyse(
                data = dataToSend,
                start = data.first().ts,
                end = data.last().ts,
                id = null,
                fusion = fusion,
                date = date
            )

        val call: Call<PostResponse> = api.postNight("Bearer $TOKEN", toSend)

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