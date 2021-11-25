package com.sleewell.sleewell.mvp.menu.profile.contract

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.api.sleewell.model.ResultRegisterSleewell

interface RegisterContract {
    interface Model {
        interface OnFinishedListener {
            /**
             * This method is call when the request is finish
             *
             * @param registerResult result for spotify
             *
             * @author gabin warnier de wailly
             */
            fun onFinished(registerResult : ResultRegisterSleewell)
            /**
             * This method is call when the request fail
             *
             * @param t error information
             *
             * @author gabin warnier de wailly
             */
            fun onFailure(t : Throwable)
        }
        fun registerToSleewellApi(onFinishedListener: OnFinishedListener, loginId: String, password: String, email: String, firstName: String, lastName: String)
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun register(loginId: String, password: String, email: String, firstName: String, lastName: String)
    }

    interface View : BaseView<Presenter> {
        fun displayToast(message: String)
        fun setAccessToken(token: String)

        fun displayLoading()
        fun hideLoading()
    }
}