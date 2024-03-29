package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.sleewell.sleewell.utils.UiAutomatorUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmUITest {
    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var mDevice: UiDevice
    private lateinit var appDrawer: UiScrollable

    private lateinit var utils: UiAutomatorUtils

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))
        context = ApplicationProvider.getApplicationContext()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        utils = UiAutomatorUtils(mDevice, appDrawer, context)

        // Launch the app
        utils.launchApp()
    }

    @Test
    fun firstPage() {
        utils.openAlarmTab()

        val createButton: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/add_alarm_button")
        )
        assert(createButton.exists())
    }

    @Test
    fun secondPage() {
        utils.openAlarmTab()

        val creationButton: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/add_alarm_button")
        )
        creationButton.click()

        assert(mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/time_picker_create_alarm")).exists())
    }
}
