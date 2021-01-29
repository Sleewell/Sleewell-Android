package com.sleewell.sleewell.mvp.protocol

import android.app.Dialog
import android.graphics.ColorFilter
import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

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
         * Convert an audio pcm buffer to spectrogram equivalent
         *
         * @param pcmAudio
         * @author Hugo Berthomé
         */
        fun convertToSpectrogram(pcmAudio: ShortArray)

        /**
         * Analyse audio and Save the results
         *
         * @author Hugo Berthomé
         */
        fun analyseAndSave(spectrogram: Array<DoubleArray>)

        /**
         * Clean up all the resources
         *
         * @author Hugo Berthomé
         */
        fun cleanUp()

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

    interface Presenter : BasePresenter, IRecorderListener, ISpectrogramListener {
        /**
         * Function to call at the creation of the view
         *
         * @author Hugo Berthomé
         */
        fun onViewCreated()

        /**
         * this method start the protocol with a specific number of repetition
         *
         * @param number the number of repetition for the halo
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
    }

    interface View : BaseView<Presenter> {
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