package com.sleewell.sleewell.mvp.menu.settings

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface SettingsContract {
    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         *
         * @author Gabin warnier de wailly
         */
        fun onViewCreated()
    }

    interface View : BaseView<Presenter>
}