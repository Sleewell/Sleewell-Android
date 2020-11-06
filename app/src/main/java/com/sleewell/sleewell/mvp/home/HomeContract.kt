package com.sleewell.sleewell.mvp.home

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView
import com.sleewell.sleewell.mvp.home.model.NfcState

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

        /**
         * Function to call on resume of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewResume()

        /**
         * Start of stop record of the sound from the device
         *
         * @author Hugo Berthomé
         */
        fun onRecordClick()

        /**
         * Start / Pause the audio player
         *
         * @author Hugo Berthomé
         */
        fun onPlayClick()
    }

    interface View : BaseView<Presenter> {
        /**
         * Display the nfc button on the screen
         *
         * @param state true - display, false - hide
         * @author Hugo Berthomé
         */
        fun displayNfcButton(state: Boolean)

        /**
         * Display if the device is currently recording or not
         *
         * @param state true - display, false - hide
         * @author Hugo Berthomé
         */
        fun displayRecordState(state: Boolean)

        /**
         * Display if the device is currently playing the record
         *
         * @param state true - display, false - hide
         * @author Hugo Berthomé
         */
        fun displayPlayerState(state: Boolean)
    }
}