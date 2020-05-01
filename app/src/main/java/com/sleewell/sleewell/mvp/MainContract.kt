package com.sleewell.sleewell.mvp

import android.net.Uri
import com.sleewell.sleewell.OpenWeather.ApiResult
import com.sleewell.sleewell.mvp.Presenter.BasePresenter
import com.sleewell.sleewell.mvp.View.BaseView

interface MainContract {

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
