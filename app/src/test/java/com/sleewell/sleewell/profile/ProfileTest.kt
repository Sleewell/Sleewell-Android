package com.sleewell.sleewell.profile

import android.app.Application
import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import com.sleewell.sleewell.mvp.menu.profile.contract.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.model.ProfileModel
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Test des appels de l'écran de profil
 * @author Titouan FIANCETTE
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileTest {

    companion object {
        const val GET_INFO_SUCCESS = "User information has been return"
        const val UPDATE_INFO_SUCCESS = "User information has been update"

        const val USER_TOKEN = "536fac7efb2a5e20026288eb9d8d59db6f5c6a79"
        const val PROFILE_USERNAME = "UnitTest"
        const val PROFILE_EMAIL = "unittest@email.com"
        const val PROFILE_FIRSTNAME = "Unit"
        const val PROFILE_LASTNAME = "Test"
    }

    @Rule
    @JvmField
    val softly = JUnitSoftAssertions()

    @Mock
    private val mockApplication: Application =  Mockito.mock(Application::class.java)

    @Mock
    private val mockContext: Context = Mockito.mock(Context::class.java)

    @Test
    fun getProfileInfo() {
        val lock = ReentrantLock()
        val condition = lock.newCondition()
        Mockito.`when`(mockApplication.applicationContext).thenReturn(mockContext)
        val model = ProfileModel(mockApplication.applicationContext)


        val token = "Bearer $USER_TOKEN"

        model.getProfileInformation(token, object : ProfileContract.Model.OnProfileInfoListener {
            override fun onFinished(profileInfo: ProfileInfo) {
                softly.assertThat(profileInfo.success).isEqualTo(GET_INFO_SUCCESS)

                softly.assertThat(profileInfo.username).isEqualTo(PROFILE_USERNAME)
                softly.assertThat(profileInfo.firstname).isEqualTo(PROFILE_FIRSTNAME)
                softly.assertThat(profileInfo.lastname).isEqualTo(PROFILE_LASTNAME)
                softly.assertThat(profileInfo.email).isEqualTo(PROFILE_EMAIL)

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }

            override fun onFailure(t: Throwable) {
                softly.fail("An error occurred when calling for profile info: " + t.message)

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }
        })

        lock.withLock {
            condition.await()     // like wait()
        }
    }

    @Test
    fun updateProfileInfo() {
        val lock = ReentrantLock()
        val condition = lock.newCondition()

        Mockito.`when`(mockApplication.applicationContext).thenReturn(mockContext)
        val model = ProfileModel(mockApplication.applicationContext)

        val token = "Bearer $USER_TOKEN"

        val username = PROFILE_USERNAME
        val firstName = PROFILE_FIRSTNAME
        val lastName = PROFILE_LASTNAME
        val email = PROFILE_EMAIL

        model.updateProfileInformation(token,
            username, firstName, lastName, email,
        object : ProfileContract.Model.OnUpdateProfileInfoListener {
            override fun onFinished(response: ResponseSuccess) {
                softly.assertThat(response.success).isEqualTo(UPDATE_INFO_SUCCESS)

                lock.withLock {
                    condition.signal() // Tell the test is finished
                }
            }

            override fun onFailure(t: Throwable) {
                softly.fail("An error occurred when calling for profile update: " + t.message)

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