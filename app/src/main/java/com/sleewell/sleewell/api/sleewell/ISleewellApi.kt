package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.MonthAnalyse
import com.sleewell.sleewell.api.sleewell.model.WeekAnalyse
import com.sleewell.sleewell.api.sleewell.model.NighAnalyse
import com.sleewell.sleewell.api.sleewell.model.YearAnalyse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ISleewellApi {
    @POST("stat")
    fun postStat(@Query("key") key: String, @Body night: NighAnalyse): Call<String>

    @GET("stat")
    fun getLastNight(@Query("key") key: String): Call<NighAnalyse>

    @GET("stat")
    fun getNight(@Query("key") key: String, @Query("night") date: String): Call<NighAnalyse>

    @GET("stat")
    fun getWeek(@Query("key") key: String, @Query("week") date: String): Call<WeekAnalyse>

    @GET("stat")
    fun getMonth(@Query("key") key: String, @Query("month") date: String): Call<MonthAnalyse>

    @GET("stat")
    fun getYear(@Query("key") key: String, @Query("year") date: String): Call<YearAnalyse>
}