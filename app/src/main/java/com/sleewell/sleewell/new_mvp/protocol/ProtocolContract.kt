package com.sleewell.sleewell.new_mvp.protocol

import android.app.Dialog
import android.graphics.ColorFilter
import com.sleewell.sleewell.new_mvp.global.BasePresenter
import com.sleewell.sleewell.new_mvp.global.BaseView

interface ProtocolContract {

    interface Model {
        /**
         * This method return the current size of the circle
         *
         * @return Int
         * @author gabin warnier de wailly
         */
        fun getSizeOfCircle() : Int

        /**
         * This method return the current color of the circle
         *
         * @return ColorFilter
         * @author gabin warnier de wailly
         */
        fun getColorOfCircle() : ColorFilter


        /**
         * This method reduce the size of the circle
         *
         * @author gabin warnier de wailly
         */
        fun degradesSizeOfCircle()

        /**
         * This method upgrade the size of the circle
         *
         * @author gabin warnier de wailly
         */
        fun upgradeSizeOfCircle()

        /**
         * This method reset the size of the circle
         *
         * @author gabin warnier de wailly
         */
        fun resetSizeOfCircle()

        /**
         * This method set up and return the color picker
         *
         * @return Dialog
         * @author gabin warnier de wailly
         */
        fun openColorPicker(): Dialog
    }

    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewCreated()

        /**
         * this method start the protocol with a specific number of repetition
         *
         * @param Int the number of repetition for the halo
         * @author gabin warnier de wailly
         */
        fun startProtocol(number: Int)

        /**
         * This method stop the current protocol
         *
         * @author gabin warnier de wailly
         */
        fun stopProtocol()

        /**
         * This method open the colorPicker directly on the view
         *
         * @author gabin warnier de wailly
         */
        fun openDialog()
    }

    interface View : BaseView<Presenter> {
        /**
         * Function that enable the phone auto lock on the activity
         *
         * @param value : true or false
         * @author Hugo Berthomé
         */
        fun enableAutoLock(value: Boolean)

        /**
         * This method display the halo with the size give in param
         *
         * @param size size of the the halo
         * @author gabin warnier de wailly
         */
        fun printHalo(size: Int)

        /**
         * This method hide the system UI for android
         *
         * @author gabin warnier de wailly
         */
        fun hideSystemUI()

        /**
         * the method set the color of the halo
         *
         * @param color color rgb for the halo
         * @author gabin warnier de wailly
         */
        fun setColorHalo(color: ColorFilter)
    }
}