package com.sleewell.sleewell.mvp.menu.statistics.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.api.sleewell.ApiClient
import com.sleewell.sleewell.api.sleewell.IStatsApi
import com.sleewell.sleewell.api.sleewell.model.ListAnalyse
import com.sleewell.sleewell.api.sleewell.model.NightAnalyse
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseDbUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.IAnalyseDataManager
import com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager.AudioAnalyseFileUtils
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
    context: AppCompatActivity,
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
    private val TAG = "StatsModel"
    private val TOKEN = MainActivity.accessTokenSleewell
    private val api: IStatsApi = ApiClient.retrofit.create(IStatsApi::class.java)


    private val analyse: IAnalyseDataManager = AudioAnalyseDbUtils(context, this)
    private var analyseFileDate = ""

    /**
     * Fetch the last analyse recorded in the phone
     *
     * @author Hugo Berthomé
     */
    override fun getLastAnalyse() {
        if (TOKEN.isEmpty()) {
            listener.onError("You're not connected, please connect and try again")
            return
        }
        analyse.getAvailableAnalyse()
    }

    /**
     * Fetch the night in the API
     *
     * @param nightDate nighDate format YYYYMMDD, if empty fetch last night
     * @author Hugo Berthomé
     */
    override fun getNight(nightDate: Date) {
        if (TOKEN.isEmpty()) {
            listener.onError("You're not connected, please connect and try again")
            return
        }
        val call: Call<NightAnalyse> =
            api.getNight("Bearer $TOKEN", dateToDateString(nightDate, FORMAT_DAY))

        call.enqueue(object : Callback<NightAnalyse> {

            override fun onResponse(call: Call<NightAnalyse>, response: Response<NightAnalyse>) {
                val responseRes: NightAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    apiListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    if (!responseRes.Error.isNullOrEmpty()) {
                        listener.onError(responseRes.Error)
                    } else {
                        apiListener.onNight(responseRes)
                    }
                }
            }

            override fun onFailure(call: Call<NightAnalyse>, t: Throwable) {
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
        /*apiListener.onNight(
            NightAnalyse(
                1617461454,
                arrayOf(
                    AnalyseValue(0.0, 1616654202),
                    AnalyseValue(32.0, 1616654220),
                    AnalyseValue(28.0, 1616654222),
                    AnalyseValue(34.0, 1616654224),
                    AnalyseValue(0.0, 1616654240)
                ),
                1617483954,
                null
            )
        )*/
    }

    /**
     * Fetch the week in the API
     *
     * @param weekDate weekDate format YYYYMMDD, if empty fetch last week
     * @author Hugo Berthomé
     */
    override fun getWeek(weekDate: Date) {
        if (TOKEN.isEmpty()) {
            listener.onError("You're not connected, please connect and try again")
            return
        }
        val call: Call<ListAnalyse> =
            api.getWeek("Bearer $TOKEN", dateToDateString(weekDate, FORMAT_WEEK))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
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
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
        /*apiListener.onWeekAnalyse(
            ListAnalyse(
                null,
                arrayOf(
                    NightAnalyse(
                        null,
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    ),
                    NightAnalyse(
                        null,
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    )
                ), 1617461454, 1617483954
            )
        )*/
    }

    /**
     * Fetch the month in the API
     *
     * @param monthDate monthDate format YYYYMM, if empty fetch last month
     * @author Hugo Berthomé
     */
    override fun getMonth(monthDate: Date) {
        if (TOKEN.isEmpty()) {
            listener.onError("You're not connected, please connect and try again")
            return
        }
        val call: Call<ListAnalyse> = api.getMonth("Bearer $TOKEN", dateToDateString(monthDate, FORMAT_MONTH))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
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
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
        /*apiListener.onMonthAnalyse(
            ListAnalyse(
                arrayOf(
                    NightAnalyse(
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    ),
                    NightAnalyse(
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    )
                ), 1617461454, 1617483954
            )
        )*/
    }

    /**
     * Fetch the year in the API
     *
     * @param yearDate yearDate format YYYY, if empty fetch last year
     */
    override fun getYear(yearDate: Date) {
        if (TOKEN.isEmpty()) {
            listener.onError("You're not connected, please connect and try again")
            return
        }
        val call: Call<ListAnalyse> = api.getYear("Bearer $TOKEN", dateToDateString(yearDate, FORMAT_YEAR))

        call.enqueue(object : Callback<ListAnalyse> {

            override fun onResponse(call: Call<ListAnalyse>, response: Response<ListAnalyse>) {
                val responseRes: ListAnalyse? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
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
                Log.e(TAG, t.toString())
                apiListener.onFailure(t)
            }

        })
        /*apiListener.onYearAnalyse(
            ListAnalyse(
                arrayOf(
                    NightAnalyse(
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    ),
                    NightAnalyse(
                        1617461454,
                        arrayOf(
                            AnalyseValue(0.0, 1616654202),
                            AnalyseValue(32.0, 1616654220),
                            AnalyseValue(28.0, 1616654222),
                            AnalyseValue(34.0, 1616654224),
                            AnalyseValue(0.0, 1616654240)
                        ),
                        1617483954,
                        null
                    )
                ), 1617461454, 1617483954
            )
        )*/
    }

    override fun onListAvailableAnalyses(analyses: List<Night>) {
        if (analyses.isEmpty()) {
            listener.onDataAnalyse(arrayOf())
        } else {
            analyseFileDate =
                AudioAnalyseFileUtils.timestampToDateString(analyses[analyses.size - 1].uId)
            analyse.readAnalyse(analyses[analyses.size - 1].uId)
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
    override fun onReadAnalyseRecord(data: Array<AnalyseValue>, nightId: Long) {
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