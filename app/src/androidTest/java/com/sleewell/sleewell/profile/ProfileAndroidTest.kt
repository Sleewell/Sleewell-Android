package com.sleewell.sleewell.profile

import android.app.Instrumentation
import android.app.Instrumentation.ActivityMonitor
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.utils.UiAutomatorUtils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileAndroidTest {

    companion object {
        const val TIME_OUT: Long = 5000

        const val USER_TOKEN = "536fac7efb2a5e20026288eb9d8d59db6f5c6a79"
        const val USER_PASSWORD = "UnitTest1234"

        const val PROFILE_USERNAME = "UnitTest"
        const val PROFILE_EMAIL = "unittest@email.com"
        const val PROFILE_FIRSTNAME = "Unit"
        const val PROFILE_LASTNAME = "Test"

        const val UPDATED_USERNAME = "TestUnit"
        const val UPDATED_FIRSTNAME = "Test"
        const val UPDATED_LASTNAME = "Unit"
    }

    private lateinit var context: Context
    private lateinit var mDevice: UiDevice
    private lateinit var appDrawer: UiScrollable
    private lateinit var mInstrumentation: Instrumentation
    private lateinit var utils: UiAutomatorUtils

    private lateinit var monitor: ActivityMonitor
    private lateinit var mainActivity: MainActivity

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))
        context = ApplicationProvider.getApplicationContext()
        mInstrumentation = getInstrumentation()
        utils = UiAutomatorUtils(mDevice, appDrawer, context)

        monitor = mInstrumentation.addMonitor(MainActivity::class.java.name, null, false)

        // Launch the app
        utils.launchApp()
    }

    @Test
    fun getProfileInfo() {
        utils.openProfileTab()

        mainActivity = monitor.waitForActivityWithTimeout(TIME_OUT) as MainActivity
        if (mainActivity.getAccessToken().isBlank()) {
            utils.loginToTestUser(PROFILE_USERNAME)
        } else if (mainActivity.getAccessToken() != USER_TOKEN) {
            utils.disconnectUser()
            utils.loginToTestUser(PROFILE_USERNAME)
        }

        assertProfileInfo()
    }

    @Test
    fun updateProfileInfo() {
        utils.openProfileTab()

        mainActivity = monitor.waitForActivityWithTimeout(TIME_OUT) as MainActivity
        if (mainActivity.getAccessToken().isBlank()) {
            utils.loginToTestUser(PROFILE_USERNAME)
        } else if (mainActivity.getAccessToken() != USER_TOKEN) {
            utils.disconnectUser()
            utils.loginToTestUser(PROFILE_USERNAME)
        }

        // Update with new info
        var usernameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/usernameEditText"))
        var firstNameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/firstNameEditText"))
        var lastNameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/lastNameEditText"))

        usernameEditText.text = UPDATED_USERNAME
        firstNameEditText.text = UPDATED_FIRSTNAME
        lastNameEditText.text = UPDATED_LASTNAME

        // save and check for update
        utils.saveProfileInfo()
        utils.disconnectUser()
        utils.loginToTestUser(UPDATED_USERNAME)

        usernameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/usernameEditText"))
        firstNameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/firstNameEditText"))
        lastNameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/lastNameEditText"))

        Assert.assertTrue(firstNameEditText.text == UPDATED_FIRSTNAME)
        Assert.assertTrue(firstNameEditText.text == UPDATED_FIRSTNAME)
        Assert.assertTrue(lastNameEditText.text == UPDATED_LASTNAME)

        // update again to go back to normal
        usernameEditText.text = PROFILE_USERNAME
        firstNameEditText.text = PROFILE_FIRSTNAME
        lastNameEditText.text = PROFILE_LASTNAME

        utils.saveProfileInfo()
        utils.disconnectUser()
        utils.loginToTestUser(PROFILE_USERNAME)

        usernameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/usernameEditText"))
        firstNameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/firstNameEditText"))
        lastNameEditText = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/lastNameEditText"))

        Assert.assertTrue(usernameEditText.text == PROFILE_USERNAME)
        Assert.assertTrue(firstNameEditText.text == PROFILE_FIRSTNAME)
        Assert.assertTrue(lastNameEditText.text == PROFILE_LASTNAME)
    }

    private fun assertProfileInfo() {
        val usernameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/usernameEditText"))
        val firstNameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/firstNameEditText"))
        val lastNameEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/lastNameEditText"))
        val emailEditText: UiObject = mDevice.findObject(UiSelector()
            .resourceId("com.sleewell.sleewell:id/emailEditText"))

        Assert.assertTrue(usernameEditText.text == PROFILE_USERNAME)
        Assert.assertTrue(firstNameEditText.text == PROFILE_FIRSTNAME)
        Assert.assertTrue(lastNameEditText.text == PROFILE_LASTNAME)
        Assert.assertTrue(emailEditText.text == PROFILE_EMAIL)
    }

    @After
    fun removeMonitor() {
        mInstrumentation.removeMonitor(monitor)
    }
}

