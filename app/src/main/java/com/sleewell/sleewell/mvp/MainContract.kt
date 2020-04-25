package com.sleewell.sleewell.mvp

import com.sleewell.sleewell.mvp.Model.Weather.WeatherState
import com.sleewell.sleewell.mvp.Presenter.BasePresenter
import com.sleewell.sleewell.mvp.View.BaseView

interface MainContract {
    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onLoadWeatherTapped()
    }

    interface View : BaseView<Presenter> {
        fun displayWeatherState(weatherState: WeatherState)
    }
}
