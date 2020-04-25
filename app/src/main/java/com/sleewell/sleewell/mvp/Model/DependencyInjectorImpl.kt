package com.sleewell.sleewell.mvp.Model

import com.sleewell.sleewell.mvp.Model.Weather.WeatherRepository
import com.sleewell.sleewell.mvp.Model.Weather.WeatherRepositoryImpl

class DependencyInjectorImpl : DependencyInjector {
    override fun weatherRepository() : WeatherRepository {
        return WeatherRepositoryImpl()
    }
}