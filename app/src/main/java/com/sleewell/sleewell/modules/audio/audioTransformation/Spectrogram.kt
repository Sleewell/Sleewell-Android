package com.sleewell.sleewell.modules.audio.audioTransformation

import kotlinx.coroutines.*
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

/**
 * Convert a PCM buffer into a spectrogram
 *
 * @property listener - CallBack for the async
 * @property sampleRate - Sample Rate
 * @property strideMs - Size in ms of a stride in the spectrogram
 * @property windowMs - Size in ms of the window in the spectrogram
 * @property maxFreq
 * @author Hugo Berthomé
 */
class Spectrogram(
    private val listener: ISpectrogramListener,
    private val sampleRate: Int,
    private val strideMs: Float = 10f,
    private val windowMs: Float = 20f,
    private val maxFreq: Int? = null
) {
    // Coroutines
    private var scopeDefault = CoroutineScope(Job() + Dispatchers.Default)
    private var queueBuffer: Queue<ShortArray> = LinkedList<ShortArray>()

    private val strideSize = (0.001 * sampleRate * strideMs).toInt()
    private val windowSize = (0.001 * sampleRate * windowMs).toInt()
    private val scale = hanningVectorSum(windowSize)

    private var leftBuffer = ShortArray(0)

    /**
     * Add the buffer in the queue of buffer to convert
     *
     * @param buffer
     * @author Hugo Berthomé
     */
    fun convertToSpectrogramAsync(buffer: ShortArray) {
        val jobStarted = queueBuffer.size != 0

        queueBuffer.add(buffer)

        // if coroutine not started, start it
        if (jobStarted) {
            scopeDefault.launch {
                while (queueBuffer.size != 0) {
                    listener.onBufferReceived(calculateSpec(queueBuffer.poll()!!))
                }
            }
        }
    }

    /**
     * Convert a buffer into his spectrogram equivalent
     *
     * @param buffer
     * @return Array<DoubleArray> Strides of values
     * @author Hugo Berthomé
     */
    fun convertToSpectrogram(buffer: ShortArray): Array<DoubleArray> {
        return calculateSpec(buffer, false)
    }

    /**
     * Function that remove all still going coroutines
     *
     * @author Hugo Berthomé
     */
    fun cleanUp() {
        scopeDefault.cancel()
    }

    /**
     * Calculate the spectrogram with the given buffer
     *
     * @param buffer
     * @param saveLeftBuffer - Save and use the unused buffer for the next upcoming buffer to convert
     * @return
     */
    private fun calculateSpec(
        buffer: ShortArray,
        saveLeftBuffer: Boolean = true
    ): Array<DoubleArray> {
        val truncatedBuffer = extractBuffer(buffer, saveLeftBuffer)
        val windows = applyHanning(splitBuffer(truncatedBuffer))

        windows.forEachIndexed { index, _ ->
            windows[index] = SoundDataUtils.fft(windows[index])
            if (index == 0 || index == windows.size - 1) {
                windows[index].forEachIndexed { j, _ ->
                    windows[index][j] /= scale
                }
            } else {
                windows[index].forEachIndexed { j, _ ->
                    windows[index][j] *= 2.0 / scale
                }
            }
        }

        return windows
    }

    /**
     * Extract from the buffer the exact size needed to transform with fft
     * Put the left data in truncatedBuffer
     *
     * @param buffer
     * @param saveLeftBuffer  True by default
     * @return The buffer to use
     */
    private fun extractBuffer(buffer: ShortArray, saveLeftBuffer: Boolean = true): ShortArray {
        val tmpBuffer = if (saveLeftBuffer) leftBuffer + buffer else buffer
        val truncateSize = tmpBuffer.size % strideSize

        if (saveLeftBuffer)
            leftBuffer = tmpBuffer.drop(tmpBuffer.size - truncateSize).toShortArray()
        return tmpBuffer.dropLast(truncateSize).toShortArray()
    }

    /**
     * Split the buffer in accordance with the window size and stride size
     *
     * @param buffer
     * @return Array<DoubleArray>
     * @author Hugo Berthomé
     */
    private fun splitBuffer(buffer: ShortArray): Array<DoubleArray> {
        val shape = ((buffer.size - windowSize) / strideSize) + 1
        val slicedBuffer = Array(shape) { DoubleArray(0) }

        for (i in 0 until shape) {
            val leftValues = buffer.drop(i * strideSize)
            slicedBuffer[i] =
                SoundDataUtils.convertShortToDouble(leftValues.dropLast(leftValues.size - windowSize))
        }
        return slicedBuffer
    }

    /**
     * Apply a window function to a list of windows
     *
     * @param windows
     * @return the result windows
     * @author Hugo Berthomé
     */
    private fun applyHanning(windows: Array<DoubleArray>): Array<DoubleArray> {
        windows.forEachIndexed { index, doubles ->
            windows[index] = applyHanning(doubles)
        }
        return windows
    }

    /**
     * Apply a window function to a window
     *
     * @param window
     * @return the result window
     * @author Hugo Berthomé
     */
    private fun applyHanning(window: DoubleArray): DoubleArray {
        window.forEachIndexed { i, value ->
            window[i] = hanning(i, window.size) * value
        }
        return window
    }

    /**
     * Sum of the hanning vector
     *
     * @param size
     * @return double
     * @author Hugo Berthomé
     */
    private fun hanningVectorSum(size: Int): Double {
        var hanningValue = 0.0

        for (i in 0 until size) {
            hanningValue += hanning(i, size).pow(2.0)
        }
        return hanningValue
    }

    /**
     * Window value fo the hanning function
     *
     * @param n
     * @param m
     * @return value
     * @author Hugo Berthomé
     */
    private fun hanning(n: Int, m: Int): Double {
        return 0.5 - (0.5 * cos((2 * PI * n) / (m - 1)))
    }
}