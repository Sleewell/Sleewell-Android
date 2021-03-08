package com.sleewell.sleewell.modules.audio.audioAnalyser.listeners

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

/**
 * Listener for the response of the Audio Analyser
 *
 * @author Hugo Berthomé
 */
interface IAudioAnalyseListener {

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    fun onError(msg : String)

    /**
     * Function called when the analyse is stopped
     *
     * @author Hugo Berthomé
     */
    fun onFinish()

    /**
     * Function called to receive the result of the analyse
     *
     * @param data
     * @author Hugo Berthomé
     */
    fun onDataAnalysed(data : AnalyseValue)

    /**
     * Function called when the DataManager has been initialized
     *
     * @author Hugo Berthomé
     */
    fun onDataManagerInitialized()
}