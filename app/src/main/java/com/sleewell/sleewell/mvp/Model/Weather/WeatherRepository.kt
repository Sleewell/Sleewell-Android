package com.sleewell.sleewell.mvp.Model.Weather

interface WeatherRepository {
    fun loadWeather(): Weather
}