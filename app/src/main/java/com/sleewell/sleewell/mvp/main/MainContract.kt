package com.sleewell.sleewell.mvp.main

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface MainContract {

    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewCreated()
    }

    interface View : BaseView<Presenter>
}