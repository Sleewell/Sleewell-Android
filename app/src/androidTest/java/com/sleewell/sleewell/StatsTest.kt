package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.sleewell.sleewell.modules.audio.audioAnalyser.AudioAnalyseFileUtils
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import com.sleewell.sleewell.utils.UiAutomatorUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 21)
class StatsTest {
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

    // Must do sound when launching this test
    @Test
    fun existingStats() {
        utils.startProtocol()
        utils.exitNetworkPanel()
        utils.phoneReturnButton()
        utils.exitNetworkPanel()
        utils.openStatTab()

        val textDate: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/textView"))

        val graph: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/AAChartView"))

        val icon: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/imageView"))

        assert(textDate.exists())
        assert(!icon.exists())
        assert(graph.exists())
    }

    @Test
    fun noStats() {
        val analyseFileUtils = AudioAnalyseFileUtils(
            context,
            object : IAudioAnalyseRecordListener {
                override fun onAnalyseRecordEnd() {
                }

                override fun onReadAnalyseRecord(data: Array<AnalyseValue>) {
                }

                override fun onAnalyseRecordError(msg: String) {
                }
            })

        analyseFileUtils.deleteAnalyses(analyseFileUtils.readDirectory())
        utils.openStatTab()

        val textDate: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/textView"))

        val graph: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/AAChartView"))

        val icon: UiObject =
            mDevice.findObject(UiSelector().resourceId("com.sleewell.sleewell:id/imageView"))

        assert(textDate.exists())
        assert(icon.exists())
    }

}