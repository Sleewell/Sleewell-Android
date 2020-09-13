package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.*
import com.sleewell.sleewell.utils.UiAutomatorUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 21)
class DndTest {
    private lateinit var context : Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var mDevice : UiDevice
    private lateinit var appDrawer : UiScrollable

    private lateinit var utils: UiAutomatorUtils

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))
        context = ApplicationProvider.getApplicationContext<Context>()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        utils = UiAutomatorUtils(mDevice, appDrawer, context)

        // Launch the app
        utils.launchApp()
    }

    @Test
    fun launchAndLeaveProtocol() {
        utils.startProtocol()

        utils.exitNetworkPanel()
        assertEquals(isDndEnable(), true)
        utils.phoneReturnButton()
        utils.exitNetworkPanel()
        assertEquals(isDndEnable(), false)
    }

    private fun isDndEnable() : Boolean
    {
        return notificationManager.currentInterruptionFilter == NotificationManager.INTERRUPTION_FILTER_ALARMS
    }
}