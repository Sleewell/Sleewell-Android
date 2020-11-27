package com.sleewell.sleewell.modules.audioRecord

import android.util.Log
import java.io.*

/**
 * AudioFile utilities to save or convert audio
 *
 * @property channelCount - Number of channel of the audio DEFAULT: 1
 * @property sampleRate - Sample rate of the audio DEFAULT: 44100
 * @property bitsPerSample - Size of the values inside the sample DEFAULT: 16
 * @author Hugo Berthomé
 */
class SoundFileUtils(
    private var channelCount: Int = 1,
    private var sampleRate: Int = 44100,
    private var bitsPerSample: Int = 16) {
    companion object {
        private const val TRANSFER_BUFFER_SIZE = 10 * 1024

        /**
         * Convert a PCM file into a WAV file
         *
         * @param input the pcm file input, limit of file size for wave file: < 2^(2*4) - 36 bytes (~4GB)
         * @param output the wav file output
         * @param channelCount number of channels: 1 for mono, 2 for stereo, etc.
         * @param sampleRate sample rate of PCM audio
         * @param bitsPerSample bits per sample, i.e. 16 for PCM16
         * @throws java.io.FileNotFoundException if the files aren't existing
         * @throws java.io.IOException – if an I/O error occurs.
         * @throws SecurityException – if a security manager exists and its checkWrite method denies write access to the file.
         * @author Hugo Berthomé
         */
        fun pcmToWav(
            input: File,
            output: File,
            channelCount: Int,
            sampleRate: Int,
            bitsPerSample: Int
        ) {
            val inputSize = input.length().toInt()
            val encoded: OutputStream = FileOutputStream(output)

            // WAVE RIFF header
            writeToOutput(encoded, "RIFF")                  // chunk id
            writeToOutput(encoded, 36 + inputSize)          // chunk size
            writeToOutput(encoded, "WAVE")                  // format

            // SUB CHUNK 1 (FORMAT)
            writeToOutput(encoded, "fmt ")                  // subchunk 1 id
            writeToOutput(encoded, 16)                      // subchunk 1 size
            writeToOutput(encoded, 1.toShort())                  // audio format (1 = PCM)
            writeToOutput(encoded, channelCount.toShort())       // number of channelCount
            writeToOutput(encoded, sampleRate)                   // sample rate
            writeToOutput(encoded, sampleRate * channelCount * bitsPerSample / 8) // byte rate
            writeToOutput(encoded, (channelCount * bitsPerSample / 8).toShort())       // block align
            writeToOutput(encoded, bitsPerSample.toShort())                            // bits per sample

            // SUB CHUNK 2 (AUDIO DATA)
            writeToOutput(encoded, "data")                  // subchunk 2 id
            writeToOutput(encoded, inputSize)                    // subchunk 2 size
            copy(FileInputStream(input), encoded)
        }

        /**
         * Write a string in the output stream
         *
         * @param output - Stream of the file
         * @param data - The string to write
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun writeToOutput(output: OutputStream, data: String) {
            output.write(data.toByteArray())
        }

        /**
         * Write an Integer in the output stream
         *
         * @param output - Stream of the file
         * @param data - The Integer to write
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun writeToOutput(output: OutputStream, data: Int) {
            output.write(data shr 0)
            output.write(data shr 8)
            output.write(data shr 16)
            output.write(data shr 24)
        }

        /**
         * Write a Short in the output stream
         *
         * @param output - Stream of the file
         * @param data - The Short to write
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun writeToOutput(output: OutputStream, data: Short) {
            val dataArray = ByteArray(2)

            dataArray[0] = (data.toInt() shr 0).toByte()
            dataArray[1] = (data.toInt() shr 8).toByte()
            output.write(dataArray)
        }

        /**
         * Copy the content of a file inside another one
         *
         * @param source - Source file to copy
         * @param output - Destination file where to write
         * @return Long - The size written
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun copy(source: InputStream, output: OutputStream): Long {
            return copy(source, output, TRANSFER_BUFFER_SIZE)
        }

        /**
         * Copy the content of a file inside another one
         *
         * @param source - Source file to copy
         * @param output - Destination file where to write
         * @param bufferSize - Size to write of the buffer
         * @return Long - size written
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun copy(source: InputStream, output: OutputStream, bufferSize: Int): Long {
            var read = 0L
            val buffer = ByteArray(bufferSize)

            var n = source.read(buffer)

            while (n != -1) {
                read += n
                output.write(buffer, 0, n)
                n = source.read(buffer)
            }

            return read
        }
    }

    private var directoryPath : String = ""
    private var fileName : String = ""
    private var outputFile : File? = null
    private var outputStream: OutputStream? = null
    private var convertToWav : Boolean = true

    /**
     * Initialise the file to save. The audio will be saved in a pcm format
     * Can be converted into
     *
     * @param outputDirectory
     * @param outputFileName
     * @param toWAV
     * @return Boolean - True if can write otherwise False
     */
    fun initSaveBuffer(outputDirectory : String, outputFileName : String, toWAV : Boolean = true) : Boolean
    {
        if (outputFile == null) {
            stopSaving()
        }

        val output = File("$outputDirectory$outputFileName.pcm")
        directoryPath = outputDirectory
        fileName = outputFileName
        outputFile = output

        try {
            outputStream = FileOutputStream(outputFile)
        } catch (e : FileNotFoundException) {
            Log.e(LOG_TAG, "Cannot create file, may be a directory")
            outputFile = null
            return false
        } catch (e : SecurityException) {
            Log.e(LOG_TAG, "Security error, check if file has write access")
            outputFile = null
            return false
        }

        convertToWav = toWAV
        return true
    }

    /**
     * Save the buffer inside the file
     *
     * @param input - ShortArray content to write inside the file
     * @return Boolean - True for success otherwise False
     * @author Hugo Berthomé
     */
    fun saveBuffer(input : ShortArray) : Boolean
    {
        if (outputFile == null)
            return false

        val bytesArray = ByteArray(input.size * 2)

        var i = 0
        for (element in input) {
            bytesArray[i] = (element.toInt() shr 0).toByte()
            bytesArray[i + 1] = (element.toInt() shr 8).toByte()
            i += 2
        }
        outputStream?.write(bytesArray)
        return true
    }

    /**
     * Close the file and convert it to wav if needed at the begining
     *
     * @return Boolean - True for success otherwise False
     * @author Hugo Berthomé
     */
    fun stopSaving() : Boolean
    {
        if (outputFile == null)
            return false

        outputStream = null
        if (convertToWav) {
            val outputFileWav = File("$directoryPath$fileName.wav")
            pcmToWav(outputFile!!, outputFileWav, channelCount, sampleRate, bitsPerSample)
            outputFile!!.delete()
        }
        outputFile = null
        return true
    }

    /**
     * Set the number of channel used when recording
     *
     * @param count
     */
    fun setChannelCount(count : Int)
    {
        channelCount = count
    }

    /**
     * Set the Sampling rate of channel used when recording
     *
     * @param rate
     */
    fun setSampleRate(rate : Int)
    {
        sampleRate = rate
    }

    /**
     * Set the sized of record per value when recording
     *
     * @param bits
     */
    fun setBitsPerSample(bits : Int)
    {
        bitsPerSample = bits
    }
}