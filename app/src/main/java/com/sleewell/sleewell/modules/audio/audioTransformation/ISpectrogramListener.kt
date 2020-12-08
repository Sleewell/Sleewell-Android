package com.sleewell.sleewell.modules.audio.audioTransformation

interface ISpectrogramListener {
    fun onBufferReceived(spectrogram : Array<DoubleArray>)

    fun onErrorSpec(msg : String)
}