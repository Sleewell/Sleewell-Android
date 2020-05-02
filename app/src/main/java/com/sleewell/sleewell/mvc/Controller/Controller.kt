package com.sleewell.sleewell.mvc.Controller

import com.sleewell.sleewell.OpenWeather.ApiResult

interface Controller {
    fun onViewCreated()
    fun onLoadWeatherTapped()
    fun manageWeatherResponse(weather: ApiResult)
}