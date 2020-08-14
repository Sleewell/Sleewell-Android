package com.sleewell.sleewell.mvp.main

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface MainContract {

    interface Presenter : BasePresenter {
        fun onViewCreated()
    }

    interface View : BaseView<Presenter>
}