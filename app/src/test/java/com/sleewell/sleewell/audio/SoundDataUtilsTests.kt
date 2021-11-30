package com.sleewell.sleewell.audio

import com.sleewell.sleewell.modules.audio.audioTransformation.SoundDataUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin

class SoundDataUtilsTests {

    @Test
    fun testMeanSquareRoot() {
        val datas = shortArrayOf(10, 25, 100, 15, 1, 26, 89, 2, 54)
        val expectedRes = 50.0
        val res = SoundDataUtils.calculateMean(datas)

        assertThat(res).isCloseTo(expectedRes, Percentage.withPercentage(1.0))
    }

    @Test
    fun testMeanSquareRootWithNegative() {
        val datas = shortArrayOf(10, 25, -100, 15, 1, -26, 89, 2, -54)
        val expectedRes = 50.0
        val res = SoundDataUtils.calculateMean(datas)

        assertThat(res).isCloseTo(expectedRes, Percentage.withPercentage(1.0))
    }

    @Test
    fun testFFT() {
        val sampleSize = 100
        val f = 3    // frequency 3
        val f2 = 11  // frequency 11

        val x = Array(sampleSize) { i -> sin(2 * PI * f * (i.toDouble() / sampleSize)) }
        val x2 = Array(sampleSize) { i -> 2 * sin(2 * PI * f2 * (i.toDouble() / sampleSize)) }
        x.forEachIndexed { index, _ ->
            x[index] += x2[index]
        }

        val res = SoundDataUtils.fft(x.toDoubleArray())
        res.forEachIndexed { index, _ ->
            when (index) {
                3 -> {
                    assertThat(res[index]).isCloseTo(50.0, Percentage.withPercentage(1.0))
                }
                11 -> {
                    assertThat(res[index]).isCloseTo(100.0, Percentage.withPercentage(1.0))
                }
                else -> {
                    assertThat(res[index]).isCloseTo(0.00001, Percentage.withPercentage(100.0))
                }
            }
        }
    }

    @Test
    fun testConvertShortToDouble() {
        val expectedRes = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0)
        val res = SoundDataUtils.convertShortToDouble(shortArrayOf(1, 2, 3, 4, 5))

        assertThat(res).isEqualTo(expectedRes)

        val res2 = SoundDataUtils.convertShortToDouble(listOf(1, 2, 3, 4, 5))
        assertThat(res2).isEqualTo(expectedRes)
    }
}