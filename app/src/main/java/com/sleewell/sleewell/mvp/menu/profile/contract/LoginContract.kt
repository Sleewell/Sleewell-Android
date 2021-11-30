package com.sleewell.sleewell.mvp.menu.profile.contract

import com.sleewell.sleewell.api.sleewell.model.ResultLoginSleewell
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface LoginContract {
    interface Model {
        interface OnFinishedListener {
            /**
             * This method is call when the request is finish
             *
             * @param loginResult result for spotify
             *
             * @author gabin warnier de wailly
             */
            fun onFinished(loginResult : ResultLoginSleewell)
            /**
             * This method is call when the request fail
             *
             * @param t error information
             *
             * @author gabin warnier de wailly
             */
            fun onFailure(t : Throwable)
        }
        fun loginToSleewell(onFinishedListener: OnFinishedListener, name : String, password: String)
        fun loginToGoogle(onFinishedListener: OnFinishedListener, token: String)
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun login(name: String, password: String)
        fun loginGoogle(token: String)
    }

    interface View : BaseView<Presenter> {
        fun displayToast(message: String)
        fun setAccessToken(token: String)

        fun displayLoading()
        fun hideLoading()
    }
}

