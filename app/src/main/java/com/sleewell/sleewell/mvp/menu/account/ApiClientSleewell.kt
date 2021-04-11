package com.sleewell.sleewell.mvp.menu.account

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClientSleewell {
    companion object {
        const val BASE_URL = "https://api.sleewell.fr/user/"

        var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        var dss = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("ds", message)
            }
        })
        var httpClient = OkHttpClient.Builder().addInterceptor(logging).addInterceptor(dss)

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
}