package com.sleewell.sleewell.mvp.protocol

import android.app.Dialog
import android.graphics.ColorFilter
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ProtocolMenuContract {
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

        /**
         * Record the audio from the mic source
         *
         * @param state
         * @author Hugo Berthomé
         */
        fun onRecordAudio(state: Boolean)

        /**
         * Return if the smartphone is recording
         *
         * @return True if recording, False otherwise
         * @author Hugo Berthomé
         */
        fun isRecording() : Boolean

        /**
         * This method start the music
         *
         * @param name name of the music
         *
         * @author gabin warnier de wailly
         */
        fun startMusique(name : String)

        /**
         * This method stop the current music launch
         *
         * @author gabin warnier de wailly
         */
        fun stopMusique()

        /**
         * This method pause the music
         *
         * @author gabin warnier de wailly
         */
        fun pauseMusique()

        /**
         * This method resume the music
         *
         * @author gabin warnier de wailly
         */
        fun resumeMusique()

        /**
         * Method to cal at the end of the view
         *
         */
        fun onDestroy()
    }

    interface Presenter : BasePresenter {
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
         * Stop the analyse
         *
         */
        fun stopAnalyse()

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