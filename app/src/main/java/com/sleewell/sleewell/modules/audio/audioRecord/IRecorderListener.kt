package com.sleewell.sleewell.modules.audio.audioRecord

/**
 * Class that will be sent to the recorder
 *
 * It will be used as callback during recording in a asynchronous way
 * @author Hugo Berthomé
 */
interface IRecorderListener {

    /**
     * When a buffer is filled, it will be sent to this callback
     *
     * @param buffer with audio data inside
     * @author Hugo Berthomé
     */
    fun onAudio(buffer: ShortArray)

    /**
     * If an error occurred, a message will be sent
     * The record will be stopped
     *
     * @param message - error message
     * @author Hugo Berthomé
     */
    fun onAudioError(message: String)

    /**
     * On finished event is called when the recording is stopped
     * (not called when an error occurred but onError instead)
     *
     * @author Hugo Berthomé
     */
    fun onAudioFinished()
}