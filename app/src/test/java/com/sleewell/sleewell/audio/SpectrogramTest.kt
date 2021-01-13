package com.sleewell.sleewell.audio

import com.sleewell.sleewell.modules.audio.audioTransformation.ISpectrogramListener
import com.sleewell.sleewell.modules.audio.audioTransformation.Spectrogram
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.PI
import kotlin.math.sin

class SpectrogramTest {

    @Rule
    @JvmField
    val softly = JUnitSoftAssertions()

    @Test
    fun testSpectrogramAsync() {
        val lock = ReentrantLock()
        val condition = lock.newCondition()

        val sampleRate = 44100
        val sampleSize = 44100

        val f = 3    // frequency 3
        val f2 = 11  // frequency 11

        val x = Array(sampleSize) { i -> (5 * sin(2 * PI * f * (i.toDouble() / sampleSize))).toInt().toShort() }
        val x2 = Array(sampleSize) { i -> (10 * sin(2 * PI * f2 * (i.toDouble() / sampleSize))).toInt().toShort() }
        x.forEachIndexed { index, _ ->
            x[index] = (x[index] + x2[index]).toShort()
        }

        val spectrogram = Spectrogram(object : ISpectrogramListener {
            override fun onBufferReceived(spectrogram: Array<DoubleArray>) {
                softly.assertThat(spectrogram.size).isEqualTo(99)
                spectrogram.forEach { it ->
                    softly.assertThat(it.size).isEqualTo(441)
                }

                lock.withLock {
                    condition.signal()
                }
            }
            override fun onErrorSpec(msg: String) {
                softly.fail("An error occurred when catching internet  : $msg")

                lock.withLock {
                    condition.signal()
                }
            }
        }, sampleRate)

        spectrogram.convertToSpectrogramAsync(x.toShortArray())

        lock.withLock {
            condition.await()     // like wait()
        }
    }

    @Test
    fun testSpectrogram() {
        val sampleRate = 44100
        val sampleSize = 44100

        val f = 3    // frequency 3
        val f2 = 11  // frequency 11

        val x = Array(sampleSize) { i -> (5 * sin(2 * PI * f * (i.toDouble() / sampleSize))).toInt().toShort() }
        val x2 = Array(sampleSize) { i -> (10 * sin(2 * PI * f2 * (i.toDouble() / sampleSize))).toInt().toShort() }
        x.forEachIndexed { index, _ ->
            x[index] = (x[index] + x2[index]).toShort()
        }

        val spectrogram = Spectrogram(object : ISpectrogramListener {
            override fun onBufferReceived(spectrogram: Array<DoubleArray>) {}
            override fun onErrorSpec(msg: String) {}
        }, sampleRate)

        val data = spectrogram.convertToSpectrogram(x.toShortArray())
        assertThat(data.size).isEqualTo(99)
        data.forEach { it ->
            assertThat(it.size).isEqualTo(441)
        }
    }
}