package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.sleewell.sleewell.utils.UiAutomatorUtils

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29)
class NetworkTest {

    private lateinit var context : Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var mDevice : UiDevice
    private lateinit var appDrawer : UiScrollable

    private lateinit var utils: UiAutomatorUtils

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))
        context = getApplicationContext()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        utils = UiAutomatorUtils(mDevice, appDrawer, context)

        // Launch the app
        utils.launchApp()
    }

    @Test
    fun launchAndLeaveProtocol() {
        utils.startProtocol()

        utils.switchWifi()
        utils.switchNetwork()
        utils.exitNetworkPanel()
        assertEquals(isNetworkEnable(), false)
        utils.phoneReturnButton()
        utils.switchWifi()
        utils.switchNetwork()
        utils.exitNetworkPanel()
        assertEquals(isNetworkEnable(), true)
    }

    private fun isNetworkEnable(): Boolean {
        val cm =
            getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        return nInfo != null && nInfo.isAvailable && nInfo.isConnected
    }
}
