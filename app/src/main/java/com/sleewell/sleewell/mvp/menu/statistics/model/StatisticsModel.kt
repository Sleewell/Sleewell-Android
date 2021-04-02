package com.sleewell.sleewell.mvp.menu.statistics.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.ISleewellApi
import com.sleewell.sleewell.api.sleewell.model.ListAnalyse
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseFileUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsModel(
    context: AppCompatActivity,
    private val listener: StatisticsContract.Model.Listener,
    private val apiListener: StatisticsContract.Model.onApiFinishedListener
) :
    StatisticsContract.Model, IAudioAnalyseRecordListener {

    private val TAG = "StatsModel"
    private val TOKEN = ""
    private val api: ISleewellApi = ApiClient.retrofit.create(ISleewellApi::class.java)
    private val FORMAT_DAY = "yyyyMMdd"
    private val FORMAT_WEEK = "yyyyMMdd"
    private val FORMAT_MONTH = "yyyyMM"
    private val FORMAT_YEAR = "yyyy"


    private val analyse: IAnalyseDataManager = AudioAnalyseDbUtils(context, this)
    private var analyseFileDate = ""

    /**
     * Fetch the last analyse recorded in the phone
     *
     * @author Hugo Berthomé
     */
    override fun getLastAnalyse() {
        analyse.getAvailableAnalyse()
    }

    /**
     * Fetch the night in the API
     *
     * @param nightDate nighDate format YYYYMMDD, if empty fetch last night
     * @author Hugo Berthomé
     */
    override fun getNight(nightDate: Date) {
        val call: Call<NightAnalyse> = api.getNight(TOKEN, dateToDateString(nightDate, FORMAT_DAY))

        call.enqueue(object : Callback<NightAnalyse> {

            override fun onResponse(call: Call<NightAnalyse>, response: Response<NightAnalyse>) {
                val responseRes: NightAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    apiListener.onNight(responseRes)
                }
            }

            override fun onFailure(call: Call<NightAnalyse>, t: Throwable) {
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
        /*apiListener.onNight(NightAnalyse(1616654202, arrayOf(AnalyseValue(1616654202.0, 1616654202)), 1616654202, null))*/
    }

    /**
     * Fetch the week in the API
     *
     * @param weekDate weekDate format YYYYMMDD, if empty fetch last week
     * @author Hugo Berthomé
     */
    override fun getWeek(weekDate: Date) {
        val call: Call<ListAnalyse> = api.getWeek(TOKEN, dateToDateString(weekDate, FORMAT_WEEK))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    apiListener.onWeekAnalyse(responseRes)
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
    }

    /**
     * Fetch the month in the API
     *
     * @param monthDate monthDate format YYYYMM, if empty fetch last month
     * @author Hugo Berthomé
     */
    override fun getMonth(monthDate: Date) {
        val call: Call<ListAnalyse> = api.getMonth(TOKEN, dateToDateString(monthDate, FORMAT_MONTH))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    apiListener.onMonthAnalyse(responseRes)
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
    }

    /**
     * Fetch the year in the API
     *
     * @param yearDate yearDate format YYYY, if empty fetch last year
     */
    override fun getYear(yearDate: Date) {
        val call: Call<ListAnalyse> = api.getWeek(TOKEN, dateToDateString(yearDate, FORMAT_YEAR))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    apiListener.onYearAnalyse(responseRes)
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
    }

    override fun onListAvailableAnalyses(analyses: List<Long>) {
        if (analyses.isEmpty()) {
            listener.onDataAnalyse(arrayOf())
        } else {
            analyseFileDate =
                AudioAnalyseFileUtils.timestampToDateString(analyses[analyses.size - 1])
            analyse.readAnalyse(analyses[analyses.size - 1])
        }
    }

    /**
     * Function called when the analyse record has stopped
     *
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordEnd() {
    }

    /**
     * Function called when an analyse is read from a file
     *
     * @param data of the analyse file
     * @author Hugo Berthomé
     */
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
        listener.onDataAnalyseDate(analyseFileDate)
        listener.onDataAnalyse(data)
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordError(msg: String) {
        listener.onError("An error occurred while reading the last available analyse")
    }

    private fun dateToDateString(date: Date, dateFormat: String = "yyyyMMdd"): String {
        return DateTimeFormatter
            .ofPattern(dateFormat)
            .withZone(ZoneOffset.systemDefault())
            .format(date.toInstant())
    }
}