package com.sleewell.sleewell.mvp.openWeather.presenter

import com.sleewell.sleewell.api.openWeather.ApiResult
import com.sleewell.sleewell.api.openWeather.Constants
import com.sleewell.sleewell.mvp.openWeather.OpenWeatherContract
import com.sleewell.sleewell.mvp.openWeather.model.OpenWeatherModel

class OpenWeatherPresenter(view: OpenWeatherContract.View) : OpenWeatherContract.Presenter,
    OpenWeatherContract.Model.OnFinishedListener {

    private var model: OpenWeatherContract.Model = OpenWeatherModel()
    private var view: OpenWeatherContract.View? = view

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
        view?.displayWaitingState()
        model.getCurrentWeather(this)
    }

    override fun onFinished(weather: ApiResult) {
        if (weather.weather != null && weather.weather!!.isNotEmpty()) {
            val path = Constants.iconBaseUrl + weather.weather!![0].icon + "@2x.png"
            view?.displayWeatherState(path)
        }
    }

    override fun onFailure(t: Throwable) {
        if (t.message != null)
            view?.showToast(t.message!!)
        else
            view?.showToast("An error occurred")
    }
}