package com.sleewell.sleewell.modules.audioRecord

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

private const val className = "Record Manager"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecorderManager(private val ctx: AppCompatActivity) : IRecorderManager{
    // Permissions
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private var recorder: MediaRecorder? = null
    private var isRecording : Boolean = false
    private var outputFile : String? = null
    private var outputFormat : Int = IRecorderManager.THREE_GPP

    /**
     * Ask the permissions to the user to use microphone
     *
     * @return true if accepted otherwise false
     * @author Hugo Berthomé
     */
    override fun askPermission() {
        ctx.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    /**
     * Check if the permission to record has been granted
     *
     * @return true if granted otherwise false
     * @author Hugo Berthomé
     */
    override fun permissionGranted(): Boolean {
        return ctx.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Start or stop recording sound from microphone
     * The output file needs to be initialised
     *
     * @param start : true - start, false - stop
     * @author Hugo Berthomé
     */
    override fun onRecord(start: Boolean) {
        if (outputFile == null)
            throw IllegalArgumentException("$className: Missing output file path")

        if (start && !isRecording)
            startRecording()
        else if (!start && isRecording)
            stopRecording()
    }

    /**
     * Return if the phone is recording sound
     *
     * @return True if recording, False if not
     * @author Hugo Berthomé
     */
    override fun isRecording(): Boolean {
        return isRecording
    }

    /**
     * Set the file output path to save the record
     *
     * @param fileName
     * @author Hugo Berthomé
     */
    override fun setOutputFile(fileName: String) {
        outputFile = fileName
    }

    /**
     * Set the output format of the file
     *
     * @param outputFormat - variable from the IRecorder.OutputFormat
     * @author Hugo Berthomé
     */
    override fun setOutputFormat(outputFormat: Int) {
        this.outputFormat = outputFormat
    }

    private fun startRecording()
    {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(outputFormat)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(className, "prepare() failed")
            }
            start()
            isRecording = true
        }
    }

    private fun stopRecording()
    {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        isRecording = false
    }
}