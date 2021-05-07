package com.sleewell.sleewell.modules.audio.audioRecord

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val LOG_TAG = "RawRecorderManager"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

/**
 * Record audio and send it inside a buffer as a PCM
 * Can save the audio in a wav file
 *
 * @property ctx
 * @property onListener
 * @property samplingRate - Default = 44100
 * @author Hugo Berthomé
 */
class RawRecorderManager(
    private val ctx: Context,
    private val onListener: IRecorderListener,
    private val samplingRate : Int = 44100
) : IRecorderManager {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    // record managing
    private var isRecording: Boolean = false

    // Files managing
    private val fileUtilities = SoundFileUtils(sampleRate = samplingRate, bitsPerSample = 16)
    private var outputDirectoryPath: String = ""
    private var outputFileName: String = ""
    private var outputFilePath: String? = null

    // Coroutine managing
    private var scopeMainThread = CoroutineScope(Job() + Dispatchers.Main)
    private var scopeIO = CoroutineScope(Job() + Dispatchers.IO)
    private var stopThread = false

    /**
     * Start the record of the audio and save it at the same time inside the file
     *
     * @author Hugo Berthomé
     */
    private fun startRecording() {
        if (isRecording) {
            onListener.onAudioError("A record is already processing")
            return
        }

        if (outputDirectoryPath.isNotEmpty() || outputFileName.isNotEmpty()) {
            if (!initFileSaving()) {
                onListener.onAudioError("An error occurred while initializing save file")
                return
            }
        }

        scopeIO.launch {

            // Before leaving job
            fun onFinishedInsideThread() {
                fileUtilities.stopSaving()
                scopeMainThread.launch {
                    onListener.onAudioFinished()
                    finishingThread()
                }
            }

            // BUFFER initialization
            var bufferSize = AudioRecord.getMinBufferSize(
                samplingRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                bufferSize = samplingRate * 2;
            }
            //val buffer: ByteBuffer = ByteBuffer.allocateDirect(bufferSize)
            val buffer: ShortArray = ShortArray(bufferSize / 2)

            // initialize recorder
            try {
                val record = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    samplingRate,
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
                    record.read(buffer, 0, buffer.size)

                    scopeMainThread.launch {
                        onListener.onAudio(buffer.clone())
                    }
                    fileUtilities.saveBuffer(buffer)
                }
                record.stop()
                record.release()
                onFinishedInsideThread()
            } catch (e: SecurityException) {
                onFinishedInsideThread()
                onListener.onAudioError("Permission to use Mic denied")
                return@launch
            }
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
            onListener.onAudioError("No record to stop")
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
    }

    /**
     * Check if the permission to record has been granted
     *
     * @return true if granted otherwise false
     * @author Hugo Berthomé
     */
    override fun permissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(ctx, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
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