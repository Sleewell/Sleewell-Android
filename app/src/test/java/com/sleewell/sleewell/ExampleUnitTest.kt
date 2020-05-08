package com.sleewell.sleewell

import android.util.Log
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.sleewell.sleewell.OpenWeather.ApiResult
import com.sleewell.sleewell.mvp.MainContract
import com.sleewell.sleewell.mvp.Model.WeatherModel
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import org.junit.rules.ErrorCollector
import org.junit.Rule
import java.lang.Exception


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Rule @JvmField
    val softly = JUnitSoftAssertions()
    
    private val model = WeatherModel()

    @Test
    fun getCurrentWeather() {
        val lock = ReentrantLock()
        val condition = lock.newCondition() // Pour pouvoir tester l'asynchrone, il va falloir créer un lock et condition
                                                     // Cela permet de gérer l'async en sync, cela fonctionne sous style de notif

        model.getCurrentWeather(object : MainContract.Model.OnFinishedListener {
            override fun onFailure(t: Throwable) {
                softly.fail("An error occurred when catching internet  : " + t.message) // Utilisation de softly pour continuer l'execution malgré un fail, sinon cela va boucle inf

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }

            override fun onFinished(weather: ApiResult) {
                softly.assertThat(1).isEqualTo(2)

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }
        })

        lock.withLock {
            condition.await()     // like wait()
        }
    }
}
