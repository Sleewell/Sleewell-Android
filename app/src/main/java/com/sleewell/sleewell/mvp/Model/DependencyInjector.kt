package com.sleewell.sleewell.mvp.Model

import com.sleewell.sleewell.mvp.Model.Weather.WeatherRepository

interface DependencyInjector {
    fun weatherRepository() : WeatherRepository
}