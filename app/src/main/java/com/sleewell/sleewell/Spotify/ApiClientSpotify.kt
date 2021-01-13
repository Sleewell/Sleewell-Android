package com.sleewell.sleewell.Spotify

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientSpotify {
    companion object {
        const val BASE_URL = "https://api.spotify.com/v1/"

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}