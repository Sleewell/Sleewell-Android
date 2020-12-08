package com.sleewell.sleewell.modules.audio.audioTransformation

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

class SoundDataUtils {
    companion object {
        fun calculateMean(shortArray: ShortArray): Double {
            var squaredSum = 0f
            for (value in shortArray) {
                squaredSum += value * value
            }
            val mean = squaredSum / shortArray.size
            return sqrt(mean.toDouble())
        }

        fun fft(shortArray: ShortArray): DoubleArray {
            val doubleArray = convertShortToDouble(shortArray)

            val ffTransformer = DoubleFFT_1D(doubleArray.size.toLong())
            ffTransformer.realForward(doubleArray)
            return extractMagnitude(doubleArray)
        }

        fun fft(doubleArray: DoubleArray) : DoubleArray {
            val ffTransformer = DoubleFFT_1D(doubleArray.size.toLong())
            ffTransformer.realForward(doubleArray)
            return extractMagnitude(doubleArray)
        }

        private fun extractMagnitude(fftArray: DoubleArray): DoubleArray {
            val n = fftArray.size
            val magnitudeArray = DoubleArray(n / 2)

            for (k in 0 until n / 2) {
                val real = fftArray[2 * k]
                var imag = fftArray[2 * k + 1]

                if (k == 0)
                    imag = 0.0

                val mag = sqrt(real * real + imag * imag)
                magnitudeArray[k] = mag
            }
            return magnitudeArray
        }

        fun convertShortToDouble(shortArray: ShortArray): DoubleArray {
            val doubleArray = DoubleArray(shortArray.size)

            shortArray.forEachIndexed { index, short ->
                doubleArray[index] = short.toDouble()
            }
            return doubleArray
        }

        fun convertShortToDouble(shortList: List<Short>): DoubleArray {
            val doubleArray = DoubleArray(shortList.size)

            shortList.forEachIndexed { index, short ->
                doubleArray[index] = short.toDouble()
            }
            return doubleArray
        }
    }
}