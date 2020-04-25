package com.sleewell.sleewell.mvp

import com.sleewell.sleewell.mvp.MainContract
import com.sleewell.sleewell.mvp.Model.DependencyInjector
import com.sleewell.sleewell.mvp.Model.Weather.Weather
import com.sleewell.sleewell.mvp.Model.Weather.WeatherRepository
import com.sleewell.sleewell.mvp.Model.Weather.WeatherState

class MainPresenter(
    view: MainContract.View,
    dependencyInjector: DependencyInjector
) : MainContract.Presenter {
    // 2
    private val weatherRepository: WeatherRepository = dependencyInjector.weatherRepository()

    // 3
    private var view: MainContract.View? = view

    override fun onDestroy() {
        this.view = null
    }

    override fun onViewCreated() {
        loadWeather()
    }

    override fun onLoadWeatherTapped() {
        loadWeather()
    }

    private fun loadWeather() {
        val weather = weatherRepository.loadWeather()
        val weatherState = weatherStateForWeather(weather)

        // Make sure to call the displayWeatherState on the view
        view?.displayWeatherState(weatherState)
    }

    private fun weatherStateForWeather(weather: Weather): WeatherState {
        if (weather.rain!!.amount!! > 0) {
            return WeatherState.RAIN
        }
        return WeatherState.SUN
    }
}
