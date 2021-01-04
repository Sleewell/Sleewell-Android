package com.sleewell.sleewell.new_mvp.mainActivity

import com.sleewell.sleewell.new_mvp.global.BasePresenter
import com.sleewell.sleewell.new_mvp.global.BaseView

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