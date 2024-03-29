package com.sleewell.sleewell.api.openWeather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun weather(@Query("q") showViral : String = "Nantes",
                @Query("appid") apiKey : String = Constants.apiKey
    ) : Call<ApiResult>
}