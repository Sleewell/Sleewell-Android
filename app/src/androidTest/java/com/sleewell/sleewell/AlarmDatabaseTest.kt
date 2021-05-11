package com.sleewell.sleewell

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.sleewell.sleewell.reveil.data.AlarmDao
import com.sleewell.sleewell.reveil.data.AlarmDatabase
import com.sleewell.sleewell.utils.UiAutomatorUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmDatabaseTest {
    private lateinit var alarmDao: AlarmDao
    private lateinit var db: AlarmDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AlarmDatabase::class.java).build()
        alarmDao = db.alarmDao()
    }
}
