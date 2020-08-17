package com.sleewell.sleewell

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 21)
class BlueToothTest {

    companion object {
        const val BASIC_SAMPLE_PACKAGE = "com.sleewell.sleewell"
        const val LAUNCH_TIMEOUT = 5000L
        const val START_BUTTON = "START"
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

        exitNetworkPanel()
        assertEquals(isBluetoothEnable(), false)
        mDevice.pressBack()

        exitNetworkPanel()
        assertEquals(isBluetoothEnable(), true)
    }

    private fun isBluetoothEnable() : Boolean
    {
        val bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bAdapter!!.isEnabled
    }

    private fun exitNetworkPanel()
    {
        val doneButton: UiObject = mDevice.findObject(UiSelector().resourceId("com.android.settings:id/done"))
        doneButton.click()
    }
}
