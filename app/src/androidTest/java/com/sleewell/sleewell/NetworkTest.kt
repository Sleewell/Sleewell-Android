package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import android.provider.Settings.Secure
import androidx.test.core.app.ApplicationProvider.getApplicationContext

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 29)
class NetworkTest {

    companion object {
        const val BASIC_SAMPLE_PACKAGE = "com.sleewell.sleewell"
        const val LAUNCH_TIMEOUT = 5000L
        const val START_BUTTON = "START"
        const val DONE_BUTTON = "Done"
    }

    private lateinit var context : Context
    private lateinit var notificationManager: NotificationManager
    private lateinit var mDevice : UiDevice
    private lateinit var appDrawer : UiScrollable

    @Before
    fun startActivity() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        appDrawer = UiScrollable(UiSelector().scrollable(true))

        // Launch the app
        context = ApplicationProvider.getApplicationContext<Context>()
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE)?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        mDevice.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    @Test
    fun launchAndLeaveProtocol() {
        val clickStart = mDevice.findObject(By.text("$START_BUTTON"))
        clickStart.click()

        switchWifi()
        switchNetwork()
        exitNetworkPanel()
        assertEquals(isNetworkEnable(), false)
        mDevice.pressBack()
        switchWifi()
        switchNetwork()
        exitNetworkPanel()
        assertEquals(isNetworkEnable(), true)
    }

    private fun isNetworkEnable() : Boolean
    {
        // TODO
        val cm = getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        val connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
        return connected
    }

    private fun exitNetworkPanel()
    {
        val doneButton: UiObject = mDevice.findObject(UiSelector().resourceId("com.android.settings:id/done"))
        doneButton.click()
    }

    private fun switchNetwork()
    {
        val switchNetwork: UiObject = mDevice.findObject(
            UiSelector().className("androidx.recyclerview.widget.RecyclerView")
                .childSelector(
                    UiSelector().className("android.widget.LinearLayout").index(1).childSelector(
                        UiSelector().className("android.widget.Switch")
                    )
                )
        )
        switchNetwork.click()
    }

    private fun switchWifi()
    {
        val switchWifi: UiObject = mDevice.findObject(
            UiSelector().className("androidx.recyclerview.widget.RecyclerView")
                .instance(0)
                .childSelector(
                    UiSelector().className("android.widget.Switch")
                )
        )
        switchWifi.click()
    }
}
