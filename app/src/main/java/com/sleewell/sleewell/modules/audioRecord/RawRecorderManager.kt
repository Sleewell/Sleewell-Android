package com.sleewell.sleewell.modules.audioRecord

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

const val LOG_TAG = "RawRecorderManager"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

/**
 * Record audio and send it inside a buffer as a PCM
 * Can save the audio in a wav file
 *
 * @property ctx
 * @property onListener
 * @author Hugo Berthomé
 */
class RawRecorderManager(
    private val ctx: AppCompatActivity,
    private val onListener: IRecorderListener
) : IRecorderManager {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    // record managing
    private var isRecording: Boolean = false
    private val SAMPLE_RATE = 44100

    // Files managing
    private val fileUtilities = SoundFileUtils(sampleRate = SAMPLE_RATE, bitsPerSample = 16)
    private var outputDirectoryPath: String = ""
    private var outputFileName: String = ""
    private var outputFilePath: String? = null
    private var outputFile: File? = null
    private var outputStream: OutputStream? = null

    // Coroutine managing
    private var job: Job = Job()
    private var scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private var scopeIO = CoroutineScope(job + Dispatchers.IO)
    private var stopThread = false

    /**
     * Start the record of the audio and save it at the same time inside the file
     *
     * @author Hugo Berthomé
     */
    private fun startRecording() {
        if (isRecording) {
            onListener.onError("A record is already processing")
            return
        }

        if (!initFileSaving()) {
            onListener.onError("An error occurred while initializing save file")
            return
        }

        scopeIO.launch {

            // Before leaving job
            fun onFinishedInsideThread() {
                fileUtilities.stopSaving()
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
                fileUtilities.saveBuffer(buffer)
            }
            record.stop()
            record.release()
            onFinishedInsideThread()
        }
        isRecording = true
    }

    /**
     * Initialize the file to save
     *
     * @return Boolean
     * @author Hugo Berthomé
     */
    private fun initFileSaving() : Boolean {
        if (outputDirectoryPath.isEmpty() || outputFileName.isEmpty())
            return false

        return fileUtilities.initSaveBuffer(outputDirectoryPath, outputFileName)
    }

    /**
     * Stop the recording of the audio
     *
     * @author Hugo Berthomé
     */
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
     * @author Hugo Berthomé
     */
    private fun finishingThread() {
        isRecording = false
        stopThread = false
        outputFile = null
        outputStream = null
    }

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
     * @param directoryPath
     * @param fileName
     * @param extension
     * @author Hugo Berthomé
     */
    override fun setOutputFile(directoryPath: String, fileName: String, extension: String) {
        outputDirectoryPath = directoryPath
        outputFileName = fileName

        outputFilePath = "$directoryPath/$fileName$extension"
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