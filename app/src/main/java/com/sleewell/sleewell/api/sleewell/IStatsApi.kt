package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.*
import retrofit2.Call
import retrofit2.http.*

interface IStatsApi {
    @POST("stats/night")
    fun postNight(
        @Header("Authorization") token: String,
        @Body night: NightAnalyse
    ): Call<PostResponse>

    @GET("stats/night")
    fun getLastNight(
        @Header("Authorization") token: String
    ): Call<NightAnalyse>

    @GET("stats/night/{nightDate}")
    fun getNight(
        @Header("Authorization") token: String,
        @Path("nightDate") nightDate: String
    ): Call<NightAnalyse>

    @GET("stats/week")
    fun getLastWeek(
        @Header("Authorization") token: String
    ): Call<ListAnalyse>

    @GET("stats/week/{weekDate}")
    fun getWeek(
        @Header("Authorization") token: String,
        @Path("weekDate") weekDate: String
    ): Call<ListAnalyse>

    @GET("stats/month")
    fun getLastMonth(
        @Header("Authorization") token: String,
        @Query("format") format: String = "week"
    ): Call<ListAnalyse>

    @GET("stats/month/{monthDate}")
    fun getMonth(
        @Header("Authorization") token: String,
        @Path("monthDate") monthDate: String,
        @Query("format") format: String = "week"
    ): Call<ListAnalyse>

    @GET("stats/year")
    fun getLastYear(
        @Header("Authorization") token: String
    ): Call<ListAnalyse>

    @GET("stats/year/{yearDate}")
    fun getYear(
        @Header("Authorization") token: String,
        @Path("yearDate") yearDate: String
    ): Call<ListAnalyse>
}