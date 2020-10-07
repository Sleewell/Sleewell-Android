package com.sleewell.sleewell.mvp.protocol

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ProtocolContract {

    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewCreated()
    }

    interface View : BaseView<Presenter> {
        /**
         * Function that enable the phone auto lock on the activity
         *
         * @param value : true or false
         * @author Hugo Berthomé
         */
        fun enableAutoLock(value: Boolean)
    }
}