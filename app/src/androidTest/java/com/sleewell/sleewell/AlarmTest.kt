package com.sleewell.sleewell

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.utils.UiAutomatorUtils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest {
    private lateinit var context: Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var mDevice: UiDevice
    private lateinit var appDrawer: UiScrollable

    private lateinit var utils: UiAutomatorUtils

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))
        context = ApplicationProvider.getApplicationContext<Context>()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        utils = UiAutomatorUtils(mDevice, appDrawer, context)

        // Launch the app
        utils.launchApp()
    }

    @Test
    fun alarmCreate() {
        utils.openAlarmTab()

        val creationButton: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/add_alarm_button")
        )
        creationButton.click()

        val validateAlarmButton: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/validate_create_alarm")
        )
        validateAlarmButton.click()

        assertEquals(isAlarmActivate(), true)
    }

    private fun isAlarmActivate() : Boolean {
        val tempIntent = Intent(context, AlarmReceiver::class.java)
        tempIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        return PendingIntent.getBroadcast(
            context,
            0,
            tempIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }
}
