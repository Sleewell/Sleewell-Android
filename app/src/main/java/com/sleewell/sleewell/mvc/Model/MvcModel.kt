package com.sleewell.sleewell.mvc.Model

import android.util.Log
import com.sleewell.sleewell.OpenWeather.ApiClient
import com.sleewell.sleewell.OpenWeather.ApiInterface
import com.sleewell.sleewell.OpenWeather.ApiResult
import com.sleewell.sleewell.mvc.Controller.MvcController
import com.sleewell.sleewell.mvc.View.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MvcModel(private val view: View, private val controller: MvcController) : Model {

    private val TAG = "WeatherModelMvp"
    private var api : ApiInterface? = ApiClient.retrofit.create(ApiInterface::class.java)

    override fun getCurrentWeather() {
        val call : Call<ApiResult>? = api?.weather()

        call?.enqueue(object : Callback<ApiResult> {

            override fun onResponse(call: Call<ApiResult>, response: Response<ApiResult>) {
                val responseRes : ApiResult? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    view.showToast("Body null error : " + response.code())
                } else {
                    controller.manageWeatherResponse(responseRes)
                }
            }


            override fun onFailure(call: Call<ApiResult>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                t.message?.let { view.showToast(it) }
            }
        })
    }

}