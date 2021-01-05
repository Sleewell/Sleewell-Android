package com.sleewell.sleewell.mvp.openWeather.model

import android.util.Log
import com.sleewell.sleewell.api.openWeather.ApiClient
import com.sleewell.sleewell.api.openWeather.ApiInterface
import com.sleewell.sleewell.api.openWeather.ApiResult
import com.sleewell.sleewell.mvp.openWeather.OpenWeatherContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherModel : OpenWeatherContract.Model{

    private val TAG = "WeatherModelMvp"
    private var api : ApiInterface? = ApiClient.retrofit.create(ApiInterface::class.java)

    override fun getCurrentWeather(onFinishedListener: OpenWeatherContract.Model.OnFinishedListener) {
        val call : Call<ApiResult>? = api?.weather()

        call?.enqueue(object : Callback<ApiResult> {

            override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                val responseRes : ApiResult? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    onFinishedListener.onFinished(responseRes)
                }
            }


            override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }
}