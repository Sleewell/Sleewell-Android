package com.sleewell.sleewell.api.sleewell

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private const val BASE_URL = Constants.apiUrl

        private var logging = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor {message ->
                //Log.d("API CLIENT", message)
            }
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val client = OkHttpClient.Builder().addInterceptor(logging).build()
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}