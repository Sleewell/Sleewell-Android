package com.sleewell.sleewell.modules.audio.audioTransformation

import com.sleewell.sleewell.modules.audio.audioRecord.IRecorderListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

class Spectrogram(
    private val listener: ISpectrogramListener,
    private val sampleRate: Int,
    private val strideMs: Float = 10f,
    private val windowMs: Float = 20f,
    private val maxFreq: Int? = null
) {
    // Coroutines
    private var job: Job = Job()
    private var scopeIO = CoroutineScope(job + Dispatchers.IO)

    private val strideSize = (0.001 * sampleRate * strideMs).toInt()
    private val windowSize = (0.001 * sampleRate * windowMs).toInt()
    private val scale = hanningVectorSum(windowSize)

    private var leftBuffer = ShortArray(0)

    private var queueBuffer: Queue<ShortArray> = LinkedList<ShortArray>()

    init {
        scopeIO.launch {
            while (true) {
                if (queueBuffer.size != 0) {
                    listener.onBufferReceived(calculateSpec(queueBuffer.poll()!!))
                    // TODO Revoir le multithread, pas optimisé pour le moment
                }
            }
        }
    }

    fun convertToSpectrogramAsync(buffer: ShortArray) {
        queueBuffer.add(buffer)
        /*scopeIO.launch {
            listener.onBufferReceived(calculateSpec(buffer))
        }*/
    }

    fun convertToSpectrogram(buffer: ShortArray): Array<DoubleArray> {
        return calculateSpec(buffer, false)
    }

    private fun calculateSpec(buffer: ShortArray, saveLeftBuffer: Boolean = true) : Array<DoubleArray> {
        val truncatedBuffer = extractBuffer(buffer, saveLeftBuffer)
        val windows = applyHanning(splitBuffer(truncatedBuffer))

        windows.forEachIndexed { index, _ ->
            windows[index] = SoundDataUtils.fft(windows[index])
            if (index == 0 || index == windows.size - 1) {
                windows[index].forEachIndexed { j, d ->
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

    private fun applyHanning(windows: Array<DoubleArray>): Array<DoubleArray> {
        windows.forEachIndexed { index, doubles ->
            windows[index] = applyHanning(doubles)
        }
        return windows
    }

    private fun applyHanning(buffer: DoubleArray): DoubleArray {
        buffer.forEachIndexed { i, value ->
            buffer[i] = hanning(i, buffer.size) * value
        }
        return buffer
    }

    private fun hanningVectorSum(size: Int): Double {
        var hanningValue = 0.0

        for (i in 0 until size) {
            hanningValue += hanning(i, size).pow(2.0)
        }
        return hanningValue
    }

    private fun hanning(n: Int, m: Int): Double {
        return 0.5 - (0.5 * cos((2 * PI * n) / (m - 1)))
    }
}