package com.sleewell.sleewell.mvp.protocol

import android.graphics.ColorFilter
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ProtocolMenuContract {
    interface Presenter : BasePresenter, IRecorderListener, ISpectrogramListener {
        /**
         * Function to call at the creation of the view
         *
         * @author Titouan FIANCETTE
         */
        fun onViewCreated()

        /**
         * Get the settings for halo display
         *
         * @return True - halo displayed | False - halo not displayed
         * @author Titouan FIANCETTE
         */
        fun isHaloOn(): Boolean

        /**
         * Plays the music at the start of the protocol
         *
         * @author Titouan FIANCETTE
         */
        fun playMusic()

        /**
         * Pauses or plays the music at the start of the protocol
         *
         * @author Titouan FIANCETTE
         */
        fun pauseMusic()

        /**
         * this method start the protocol with a specific number of repetition
         *
         * @param number the number of repetition for the halo
         * @author gabin warnier de wailly
         */
        fun startHalo()

        /**
         * This method stop the current protocol
         *
         * @author gabin warnier de wailly
         */
        fun stopHalo()

        /**
         * Start the sleep analyse
         * Will record audio, analyse and save the data from the night in a file
         *
         * @author Hugo Berthomé
         */
        fun startAnalyse()

        /**
         * Pause the sleep analyse
         *
         * @author Hugo Berthomé
         */
        fun pauseAnalyse()

        /**
         * Resume the paused sleep analyse
         *
         * @author Hugo Berthomé
         */
        fun resumeAnalyse()

        /**
         * Remove the show when locked flag to the activity
         *
         * @author Titouan FIANCETTE
         */
        fun disableShowWhenLock()
    }

    interface View : BaseView<Presenter> {
        /**
         * Initialise all the widgets from the layout
         *
         * @author Titouan FIANCETTE
         */
        fun initActivityWidgets()

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

        /**
         * Returns if the music is being played
         *
         * @return state music is playing
         * @author Titouan FIANCETTE
         */
        fun isMusicPlaying(): Boolean

        /**
         * Animates or stops the equalizer
         *
         * @param color color rgb for the halo
         * @author Titouan FIANCETTE
         */
        fun animateEqualizer(state: Boolean)
    }
}