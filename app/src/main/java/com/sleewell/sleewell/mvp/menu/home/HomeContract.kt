package com.sleewell.sleewell.mvp.menu.home

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.menu.home.model.NfcState

/**
 * Contract that defines all the functions that will interact with the user
 * @author Hugo Berthomé
 */
interface HomeContract {
    interface Model {
        /**
         * Get the state of the nfc module
         *
         * @return NfcState enum
         * @author Hugo Berthomé
         */
        fun nfcState() : NfcState
    }

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