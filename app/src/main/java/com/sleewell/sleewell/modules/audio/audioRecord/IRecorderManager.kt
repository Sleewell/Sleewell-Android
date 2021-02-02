package com.sleewell.sleewell.modules.audio.audioRecord

import android.media.MediaRecorder

interface IRecorderManager {
    companion object OutputFormat {
        val AAC_ADTS: Int =  MediaRecorder.OutputFormat.AAC_ADTS
        val AMR_NB: Int =  MediaRecorder.OutputFormat.AMR_NB
        val AMR_WB: Int =  MediaRecorder.OutputFormat.AMR_WB
        val DEFAULT: Int =  MediaRecorder.OutputFormat.DEFAULT
        val MPEG_4: Int =  MediaRecorder.OutputFormat.MPEG_4
        val THREE_GPP: Int =  MediaRecorder.OutputFormat.THREE_GPP
        val WEBM: Int =  MediaRecorder.OutputFormat.WEBM
//        val OGG: Int =  MediaRecorder.OutputFormat.OGG  //Require API 29
//        val MPEG_2_TS: Int =  MediaRecorder.OutputFormat.MPEG_2_TS //Require API 29
    }

    /**
     * Check if the permission to record has been granted
     *
     * @return true if granted oterwise false
     * @author Hugo Berthomé
     */
    fun permissionGranted() : Boolean

    /**
     * Start or stop recording sound from microphone
     * The output file needs to be initialised
     *
     * @param start : true - start, false - stop
     * @author Hugo Berthomé
     */
    fun onRecord(start: Boolean)

    /**
     * Return if the phone is recording sound
     *
     * @return True if recording, False if not
     * @author Hugo Berthomé
     */
    fun isRecording() : Boolean

    /**
     * Set the file output path to save the record
     *
     * @param directoryPath
     * @param fileName
     * @param extension
     * @author Hugo Berthomé
     */
    fun setOutputFile(directoryPath: String, fileName: String, extension: String = ".pcm")

    /**
     * Set the output format of the file
     *
     * @param outputFormat - variable from the IRecorder.OutputFormat
     * @author Hugo Berthomé
     */
    fun setOutputFormat(outputFormat: Int)
}