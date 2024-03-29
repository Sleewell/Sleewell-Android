package com.sleewell.sleewell.mvp.menu.statistics.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IStatsApi
import com.sleewell.sleewell.api.sleewell.model.ListAnalyse
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.menu.statistics.StatisticsContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsModel(
    val context: AppCompatActivity,
    private val listener: StatisticsContract.Model.Listener,
    private val apiListener: StatisticsContract.Model.OnApiFinishedListener
) :
    StatisticsContract.Model, IAudioAnalyseRecordListener {

    companion object {
        val FORMAT_DAY = "yyyyMMdd"
        val FORMAT_WEEK = "yyyyMMdd"
        val FORMAT_MONTH = "yyyyMM"
        val FORMAT_YEAR = "yyyy"
    }

    private val tag = "StatsModel"
    private val token = MainActivity.accessTokenSleewell
    private val api: IStatsApi = ApiClient.retrofit.create(IStatsApi::class.java)

    private val analyse: IAnalyseDataManager = AudioAnalyseDbUtils(context, this)
    private var analyseFileDate = ""
    private var errorMsg = ""
    private var errorOrFailure = true

    /**
     * Fetch the night in the API
     *
     * @param nightDate nighDate format YYYYMMDD, if empty fetch last night
     * @author Hugo Berthomé
     */
    override fun getNight(nightDate: Date) {
        analyseFileDate = dateToDateString(nightDate)
        if (token.isEmpty()) {
            errorMsg = context.getString(R.string.stats_error_log)
            errorOrFailure = true
            getLocalAnalyseFromDate(nightDate)
            return
        }
        val call: Call<NightAnalyse> =
            api.getNight("Bearer $token", dateToDateString(nightDate, FORMAT_DAY))

        call.enqueue(object : Callback<NightAnalyse> {

            override fun onResponse(call: Call<NightAnalyse>, response: Response<NightAnalyse>) {
                val responseRes: NightAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    errorMsg = "Body null error : " + response.code()
                    errorOrFailure = false
                    getLocalAnalyseFromDate(nightDate)
                } else {
                    if (!responseRes.Error.isNullOrEmpty()) {
                        errorMsg = responseRes.Error
                        errorOrFailure = true
                        getLocalAnalyseFromDate(nightDate)
                    } else {
                        apiListener.onNight(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<NightAnalyse>, t: Throwable) {
                Log.e(tag, t.toString())
                errorMsg =
                    if (t.message == null) context.getString(R.string.stats_error_log) else t.message.toString()
                errorOrFailure = false
                getLocalAnalyseFromDate(nightDate)
            }

        })
    }

    /**
     * Fetch the week in the API
     *
     * @param weekDate weekDate format YYYYMMDD, if empty fetch last week
     * @author Hugo Berthomé
     */
    override fun getWeek(weekDate: Date) {
        if (token.isEmpty()) {
            listener.onError(context.getString(R.string.stats_error_log))
            return
        }
        val call: Call<ListAnalyse> =
            api.getWeek("Bearer $token", dateToDateString(weekDate, FORMAT_WEEK))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    if (!responseRes.Error.isNullOrEmpty()) {
                        listener.onError(responseRes.Error)
                    } else {
                        apiListener.onWeekAnalyse(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(tag, t.toString())
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
        if (token.isEmpty()) {
            listener.onError(context.getString(R.string.stats_error_log))
            return
        }
        val call: Call<ListAnalyse> =
            api.getMonth("Bearer $token", dateToDateString(monthDate, FORMAT_MONTH))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    if (!responseRes.Error.isNullOrEmpty()) {
                        listener.onError(responseRes.Error)
                    } else {
                        apiListener.onMonthAnalyse(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(tag, t.toString())
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
        if (token.isEmpty()) {
            listener.onError(context.getString(R.string.stats_error_log))
            return
        }
        val call: Call<ListAnalyse> =
            api.getYear("Bearer $token", dateToDateString(yearDate, FORMAT_YEAR))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(tag, "Body null error")
                    Log.e(tag, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    if (!responseRes.Error.isNullOrEmpty()) {
                        listener.onError(responseRes.Error)
                    } else {
                        apiListener.onYearAnalyse(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<ListAnalyse>, t: Throwable) {
                Log.e(tag, t.toString())
                apiListener.onFailure(t)
            }

        })
    }

    override fun onListAvailableAnalyses(analyses: List<Night>) {
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
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>, nightId: Long) {
        listener.onDataAnalyseDate(analyseFileDate)
        if (data.isNullOrEmpty()) {
            apiListener.onNight(NightAnalyse(start = 0, data = data, end = 0, id = null))
        } else {
            apiListener.onNight(
                NightAnalyse(
                    start = data.first().ts,
                    data = data,
                    end = data.last().ts,
                    id = null
                )
            )
        }
        /*listener.onDataAnalyse(data)*/
    }

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    override fun onAnalyseRecordError(msg: String) {
        if (errorOrFailure)
            listener.onError(errorMsg)
        else
            apiListener.onFailure(Throwable(errorMsg))
    }

    private fun dateToDateString(date: Date, dateFormat: String = "yyyyMMdd"): String {
        return DateTimeFormatter
            .ofPattern(dateFormat)
            .withZone(ZoneOffset.systemDefault())
            .format(date.toInstant())
    }

    private fun getLocalAnalyseFromDate(nightDate: Date) {
        analyse.readAnalyse(nightDate)
    }
}