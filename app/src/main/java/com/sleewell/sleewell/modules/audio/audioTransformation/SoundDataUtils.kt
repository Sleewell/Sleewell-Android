package com.sleewell.sleewell.modules.audio.audioTransformation

import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.sqrt

/**
 * A utility class with several functions that can be used on Audio data
 *
 * @author Hugo Berthomé
 */
class SoundDataUtils {
    companion object {
        /**
         * Calculate the mean square root average of an array
         *
         * @param shortArray
         * @return double value
         * @author Hugo Berthomé
         */
        fun calculateMean(shortArray: ShortArray): Double {
            var squaredSum = 0f
            for (value in shortArray) {
                squaredSum += value * value
            }
            val mean = squaredSum / shortArray.size
            return sqrt(mean.toDouble())
        }

        /**
         * Apply a Fast Fourier Transformation to a PCM array
         *
         * @param shortArray PCM data
         * @return a double array
         * @author Hugo Berthomé
         */
        fun fft(shortArray: ShortArray): DoubleArray {
            val doubleArray = convertShortToDouble(shortArray)

            val ffTransformer = DoubleFFT_1D(doubleArray.size.toLong())
            ffTransformer.realForward(doubleArray)
            return extractMagnitude(doubleArray)
        }

        /**
         * Apply a Fast Fourier Transformation to a PCM array
         *
         * @param doubleArray PCM data
         * @return a double array
         * @author Hugo Berthomé
         */
        fun fft(doubleArray: DoubleArray): DoubleArray {
            val ffTransformer = DoubleFFT_1D(doubleArray.size.toLong())
            ffTransformer.realForward(doubleArray)
            return extractMagnitude(doubleArray)
        }

        /**
         * Extract the magnitude from the Fast fourier transform array
         *
         * @param fftArray
         * @return DoubleArray
         */
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

        /**
         * Convert ShortArray to DoubleArray
         *
         * @param shortArray
         * @return
         */
        fun convertShortToDouble(shortArray: ShortArray): DoubleArray {
            val doubleArray = DoubleArray(shortArray.size)

            shortArray.forEachIndexed { index, short ->
                doubleArray[index] = short.toDouble()
            }
            return doubleArray
        }

        /**
         * Convert ShortList to DoubleArray
         *
         * @param shortList
         * @return
         */
        fun convertShortToDouble(shortList: List<Short>): DoubleArray {
            val doubleArray = DoubleArray(shortList.size)

            shortList.forEachIndexed { index, short ->
                doubleArray[index] = short.toDouble()
            }
            return doubleArray
        }
    }
}