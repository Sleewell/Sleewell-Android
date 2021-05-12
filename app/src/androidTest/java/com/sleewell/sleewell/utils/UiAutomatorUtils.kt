package com.sleewell.sleewell.utils

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.test.uiautomator.*
import java.lang.Thread.sleep

class UiAutomatorUtils(
    private val mDevice: UiDevice,
    private val appDrawer: UiScrollable,
    private val context: Context
) {
    companion object {
        const val BASIC_SAMPLE_PACKAGE = "com.sleewell.sleewell"
        const val SETTING_PANEL_PACKAGE = "com.android.settings"
        const val LAUNCH_TIMEOUT = 5000L
        const val START_BUTTON = "START"
        const val DONE_BUTTON = "Done"
    }

    fun launchApp() {
        // Launch the app
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE
        )?.apply {
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

    fun leaveApp() {
        mDevice.pressHome()
    }

    fun phoneReturnButton() {
        mDevice.pressBack()
    }

    fun openSettings() {
        val buttonSetting: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/settings_nav")
        )
        buttonSetting.click()
    }

    fun openNotificationSettings() {
        val notifyButton: UiObject = mDevice.findObject(
            UiSelector().className("androidx.recyclerview.widget.RecyclerView")
                .instance(0)
                .childSelector(
                    UiSelector().className("android.widget.LinearLayout").index(1)
                )
        )
        notifyButton.click()
    }

    fun openNetworkSettings() {
        val networkButton: UiObject = mDevice.findObject(
            UiSelector().className("androidx.recyclerview.widget.RecyclerView")
                .instance(0)
                .childSelector(
                    UiSelector().className("android.widget.LinearLayout").index(0)
                )
        )
        networkButton.click()
    }

    fun startProtocol() {
        val clickStart = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/button_protocol")
        )
        clickStart.click()
    }

    fun startProtocolNfc() {
        // TODO ne marche plus car on est dans un fragment à présent
        val intent = Intent(NfcAdapter.ACTION_NDEF_DISCOVERED)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.type = "application/com.sleewell.sleewell"
        context.startActivity(intent)
    }

    fun switchWifi() {
        val switchWifi: UiObject = mDevice.findObject(
            UiSelector().className("androidx.recyclerview.widget.RecyclerView")
                .instance(0)
                .childSelector(
                    UiSelector().className("android.widget.Switch")
                )
        )
        switchWifi.click()
    }

    fun switchNetwork() {
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

    fun exitNetworkPanel() {
        val doneButton: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.android.settings:id/done"))
        doneButton.click()
    }

    fun openStatTab() {
        val buttonSetting: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/stats_nav")
        )
        buttonSetting.click()
    }

    fun openAlarmTab() {
        val alarmButton: UiObject = mDevice.findObject(
            UiSelector().resourceId("com.sleewell.sleewell:id/alarm_nav")
        )
        alarmButton.click()
    }

}