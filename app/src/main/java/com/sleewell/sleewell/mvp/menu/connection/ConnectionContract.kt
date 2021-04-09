package com.sleewell.sleewell.mvp.menu.connection

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ConnectionContract {
    interface Model {
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
    }

    interface View : BaseView<Presenter> {
        fun displayToast(message: String)
    }
}

