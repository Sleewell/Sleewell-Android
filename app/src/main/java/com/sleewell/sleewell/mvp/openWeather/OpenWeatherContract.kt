package com.sleewell.sleewell.mvp.openWeather

import com.sleewell.sleewell.api.openWeather.ApiResult
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface OpenWeatherContract {

    interface Model {

        interface OnFinishedListener {
            fun onFinished(weather : ApiResult)
            fun onFailure(t : Throwable)
        }

        fun getCurrentWeather(onFinishedListener: OnFinishedListener)
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun onLoadWeatherTapped()
    }

    interface View : BaseView<Presenter> {
        fun displayWeatherState(imageUrl: String)

        fun displayWaitingState()

        fun showToast(msg: String)
    }
}