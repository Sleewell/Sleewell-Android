package com.sleewell.sleewell.mvc.Controller

import com.sleewell.sleewell.Constants
import com.sleewell.sleewell.OpenWeather.ApiResult
import com.sleewell.sleewell.mvc.Model.Model
import com.sleewell.sleewell.mvc.Model.MvcModel
import com.sleewell.sleewell.mvc.View.View

class MvcController(private val view: View) : Controller {
    private val model: Model = MvcModel(view, this)

    override fun manageWeatherResponse(weather: ApiResult) {
        if (weather.weather != null && weather.weather!!.isNotEmpty()) {
            val path = Constants.iconBaseUrl + weather.weather!![0].icon + "@2x.png"
            view.displayWeatherState(path)
        }
    }

    override fun onViewCreated() {
        onLoadWeatherTapped()
    }

    override fun onLoadWeatherTapped() {
        model.getCurrentWeather()
        view.displayWaitingState()
    }
}