package com.sleewell.sleewell.modules.audioRecord

import org.apache.commons.math3.analysis.function.Sqrt
import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

class SoundDataUtils {
    companion object {
        fun calculateMean(shortArray: ShortArray) : Double {
            var squaredSum = 0f
            for (value in shortArray) {
                squaredSum += value * value
            }
            val mean = squaredSum / shortArray.size
            return sqrt(mean.toDouble())
        }

        fun fft(shortArray: ShortArray) {
            val doubleArray = convertShortToDouble(shortArray)

            // for the example, will do with a sinusoidal function to check if the results are right
            /*val samples = 100
            val f = 3.0
            val doubleArray = DoubleArray(samples) {
                sin(2 * PI * f * (it.toDouble() / samples.toDouble()))
            }*/

            val ffTransformer = DoubleFFT_1D(doubleArray.size.toLong())

            ffTransformer.realForward(doubleArray)

            val magnitude = extractMagnitude(doubleArray)
        }

        private fun extractMagnitude(fftArray: DoubleArray) : DoubleArray
        {
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

        private fun convertShortToDouble(shortArray: ShortArray): DoubleArray {
            val doubleArray = DoubleArray(shortArray.size)

            shortArray.forEachIndexed { index, short ->
                //doubleArray[index] = short.toDouble() / Short.MIN_VALUE * -1
                doubleArray[index] = short.toDouble()
            }
            return doubleArray
        }
    }
}