package com.sleewell.sleewell.modules.audio.audioTransformation

/**
 * Function for the callback for the spectrogram class
 *
 */
interface ISpectrogramListener {

    /**
     * Function called when a list of spectrogram windows has been calculated
     *
     * @param spectrogram
     */
    fun onBufferReceived(spectrogram : Array<DoubleArray>)

    /**
     * Function called when an error occurred
     *
     * @param msg
     */
    fun onErrorSpec(msg : String)
}