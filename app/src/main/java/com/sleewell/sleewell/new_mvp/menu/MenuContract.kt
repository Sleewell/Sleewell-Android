package com.sleewell.sleewell.new_mvp.menu

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.home.model.NfcState

/**
 * Contract that defines all the functions that will interact with the user
 * @author Hugo Berthomé
 */
interface MenuContract {
    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewCreated()
    }
}