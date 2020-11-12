package com.sleewell.sleewell.modules.audioRecord

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder

const val LOG_TAG = "RawRecorderManager"

class RawRecorderManager(
    private val ctx: AppCompatActivity,
    private val onListener: IRecorderListener
) : IRecorderManager {

    private val SAMPLE_RATE = 44100

    private var isRecording: Boolean = false

    private var job: Job = Job()
    private var scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private var scopeIO = CoroutineScope(job + Dispatchers.IO)
    private var stopThread = false

    private fun startRecording() {
        if (isRecording) {
            onListener.onError("A record is already processing")
            return
        }

        scopeIO.launch {

            // Before leaving job
            fun onFinishedInsideThread() {
                scopeMainThread.launch {
                    onListener.onFinished()
                    finishingThread()
                }
            }


            // BUFFER initialization
            var bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                bufferSize = SAMPLE_RATE * 2;
            }
            //val buffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)
            val buffer: ShortArray = ShortArray(bufferSize / 2)

            // initialize recorder
            val record = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
            if (record.state != AudioRecord.STATE_INITIALIZED) {
                Log.e(LOG_TAG, "Audio Record can't initialize!");
                onFinishedInsideThread()
                return@launch
            }

            // Start recording
            record.startRecording();
            Log.v(LOG_TAG, "Start recording");
            while (!stopThread) {
                val numberOfShort: Int = record.read(buffer, 0, buffer.size)

                scopeMainThread.launch {
                    onListener.onAudio(buffer.clone())
                }
            }
            record.stop();
            record.release();
            onFinishedInsideThread()
        }
        isRecording = true
    }

    private fun stopRecording() {
        if (!isRecording) {
            onListener.onError("No record to stop")
            return
        }

        stopThread = true
    }

    /**
     * Function called when the thread is finished
     *
     */
    private fun finishingThread() {
        isRecording = false
        stopThread = false
    }

    /**
     * Ask the permissions to the user to use microphone
     *
     * @return true if accepted otherwise false
     * @author Hugo Berthomé
     */
    override fun askPermission() {
        TODO("Not yet implemented")
    }

    /**
     * Check if the permission to record has been granted
     *
     * @return true if granted otherwise false
     * @author Hugo Berthomé
     */
    override fun permissionGranted(): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Start or stop recording sound from microphone
     * The output file needs to be initialised
     *
     * @param start : true - start, false - stop
     * @author Hugo Berthomé
     */
    override fun onRecord(start: Boolean) {
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
        return this.isRecording
    }

    /**
     * Set the file output path to save the record
     *
     * @param fileName
     * @author Hugo Berthomé
     */
    override fun setOutputFile(fileName: String) {
        // Do nothing with the raw recording
    }

    /**
     * Set the output format of the file
     *
     * @param outputFormat - variable from the IRecorder.OutputFormat
     * @author Hugo Berthomé
     */
    override fun setOutputFormat(outputFormat: Int) {
        // Do nothing with the raw recording
    }
}