package com.sleewell.sleewell.mvp.Model.Weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class WeatherRepositoryImpl : WeatherRepository {
    val jsonRain:String = "{\"id\":804,\"main\":\"clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\", \"rain\":{\"amount\":20},\"clouds\":{\"all\":92}, \"wind\":{\"speed\":7.31,\"deg\":187.002}, \"temp\":289.5,\"humidity\":89,\"pressure\":1013,\"temp_min\":287.04,\"temp_max\":292.04}"
    val jsonSun:String = "{\"id\":804,\"main\":\"sunny\",\"description\":\"sunny\",\"icon\":\"04n\", \"rain\":{\"amount\":0},\"clouds\":{\"all\":92}, \"wind\":{\"speed\":7.31,\"deg\":187.002}, \"temp\":289.5,\"humidity\":89,\"pressure\":1013,\"temp_min\":287.04,\"temp_max\":292.04}"

    override fun loadWeather(): Weather {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val json = when((1..2).shuffled().last()) {
            1 -> jsonRain
            else -> jsonSun
        }

        val jsonAdapter = moshi.adapter<Weather>(Weather::class.java)

        val weather: Weather = jsonAdapter.fromJson(json)!!

        return weather
    }
}
