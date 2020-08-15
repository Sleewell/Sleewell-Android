package com.sleewell.sleewell

import com.sleewell.sleewell.api.openWeather.ApiResult
import com.sleewell.sleewell.mvp.openWeather.OpenWeatherContract
import com.sleewell.sleewell.mvp.openWeather.model.OpenWeatherModel
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Rule @JvmField
    val softly = JUnitSoftAssertions()

    private val model = OpenWeatherModel()

    @Test
    fun getCurrentWeather() {
        val lock = ReentrantLock()
        val condition = lock.newCondition() // Pour pouvoir tester l'asynchrone, il va falloir créer un lock et condition
        // Cela permet de gérer l'async en sync, cela fonctionne sous style de notif

        model.getCurrentWeather(object : OpenWeatherContract.Model.OnFinishedListener {
            override fun onFailure(t: Throwable) {
                softly.fail("An error occurred when catching internet  : " + t.message) // Utilisation de softly pour continuer l'execution malgré un fail, sinon cela va boucle inf

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }

            override fun onFinished(weather: ApiResult) {
                softly.assertThat(1).isEqualTo(1)

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