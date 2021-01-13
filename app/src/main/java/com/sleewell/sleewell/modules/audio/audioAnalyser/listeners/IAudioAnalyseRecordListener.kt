package com.sleewell.sleewell.modules.audio.audioAnalyser.listeners

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

/**
 * Interface for listening to the response of the Audio Analyse recorder
 *
 * @author Hugo Berthomé
 */
interface IAudioAnalyseRecordListener {

    /**
     * Function called when the analyse record has stopped
     *
     * @author Hugo Berthomé
     */
    fun onAnalyseRecordEnd()

    /**
     * Function called when an analyse is read from a file
     *
     * @param data of the analyse file
     * @author Hugo Berthomé
     */
    fun onReadAnalyseRecord(data : Array<AnalyseValue>)

    /**
     * Function called when an error occur
     *
     * @param msg to display
     * @author Hugo Berthomé
     */
    fun onAnalyseRecordError(msg : String)
}