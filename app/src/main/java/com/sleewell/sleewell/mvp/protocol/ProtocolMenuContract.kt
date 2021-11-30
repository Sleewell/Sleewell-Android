package com.sleewell.sleewell.mvp.protocol

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface ProtocolMenuContract {
    interface Model {

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
         * This method stop the current music launch
         *
         * @author gabin warnier de wailly
         */
        fun stopMusic()

        /**
         * This method pause the music
         *
         * @author gabin warnier de wailly
         */
        fun pauseMusic()

        /**
         * This method resume the music
         *
         * @author gabin warnier de wailly
         */
        fun resumeMusic()

        /**
         * Method to cal at the end of the view
         *
         * @author gabin warnier de wailly
         */
        fun onDestroy()

        /**
         * go search in database the routine selected and set parameters
         *
         * @param startRoutine
         *
         * @author gabin warnier de wailly
         */
        fun setRoutineSelected(startRoutine: () -> Unit)

        /**
         *
         * @return if use halo in routine
         *
         * @author gabin warnier de wailly
         */
        fun routineUseHalo(): Boolean

        /**
         * @return if use music in routine
         */
        fun routineUseMusic(): Boolean

        /**
         *
         * @return color for halo in routine
         *
         * @author gabin warnier de wailly
         */
        fun getroutineColorHalo(): Int

        /**
         *
         * @return player for music in routine
         *
         * @author gabin warnier de wailly
         */
        fun getRoutinePlayer(): String

        /**
         * Login to Spotify and play music directly
         *
         * @author gabin warnier de wailly
         */
        fun loginSpotify()

        /**
         * Play music form routine
         *
         * @author gabin warnier de wailly
         */
        fun playMusic()
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
         * This method hide the system UI for android
         *
         * @author gabin warnier de wailly
         */
        fun hideSystemUI()

        /**
         * This method shows the system UI for android
         *
         * @author Titouan FIANCETTE
         */
        fun showSystemUI()

        /**
         * the method set the color of the halo
         *
         * @param color color rgb for the halo
         * @author gabin warnier de wailly
         */
        fun setHaloColor(color: Int)

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
         * @param state
         * @author Titouan FIANCETTE
         */
        fun animateEqualizer(state: Boolean)
        fun haloDisplayLooper()
        fun stopAnimation()
        fun undisplayEquilizer()
    }
}