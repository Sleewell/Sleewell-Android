package com.sleewell.sleewell.mvp.menu.account.contract

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.menu.account.ApiResultLoginSleewell

interface ConnectionContract {
    interface Model {
        interface OnFinishedListener {
            /**
             * This method is call when the request is finish
             *
             * @param weather result for spotify
             *
             * @author gabin warnier de wailly
             */
            fun onFinished(loginResult : ApiResultLoginSleewell)
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
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun login(name: String, password: String)
    }

    interface View : BaseView<Presenter> {
        fun displayToast(message: String)
        fun setAccessToken(token: String)
    }
}

