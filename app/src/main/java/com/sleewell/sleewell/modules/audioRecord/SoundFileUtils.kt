package com.sleewell.sleewell.modules.audioRecord

import android.R.attr
import java.io.*
import java.nio.ByteBuffer

class SoundFileUtils {
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
         * TODO
         *
         * @param output
         * @param data
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun writeToOutput(output: OutputStream, data: String) {
            output.write(data.toByteArray())
        }

        /**
         * TODO
         *
         * @param output
         * @param data
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
         * TODO
         *
         * @param output
         * @param data
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
         * TODO
         *
         * @param source
         * @param output
         * @return Long
         * @throws java.io.IOException – if an I/O error occurs.
         * @author Hugo Berthomé
         */
        private fun copy(source: InputStream, output: OutputStream): Long {
            return copy(source, output, TRANSFER_BUFFER_SIZE)
        }

        /**
         * TODO
         *
         * @param source
         * @param output
         * @param bufferSize
         * @return
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
}