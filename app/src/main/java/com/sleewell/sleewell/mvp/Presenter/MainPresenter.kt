package com.sleewell.sleewell.mvp.Presenter

import com.sleewell.sleewell.Constants
import com.sleewell.sleewell.OpenWeather.ApiResult
import com.sleewell.sleewell.mvp.MainContract
import com.sleewell.sleewell.mvp.Model.WeatherModel

class MainPresenter(view: MainContract.View) : MainContract.Presenter,
    MainContract.Model.OnFinishedListener {

    private var model: MainContract.Model = WeatherModel()
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
