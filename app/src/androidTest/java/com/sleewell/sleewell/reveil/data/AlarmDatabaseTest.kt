package com.sleewell.sleewell.reveil.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sleewell.sleewell.reveil.data.model.Alarm
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AlarmDatabaseTest : TestCase() {
    private lateinit var db: AlarmDatabase
    private lateinit var dao: AlarmDao

    @Before
    public override fun setUp() {
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        db = Room.databaseBuilder(context, AlarmDatabase::class.java, "alarm_table").build()
        dao = db.alarmDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadAlarm() = runBlocking {
        val copy = mutableListOf(
            false
        )
        val uniqueId = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        val alarm = Alarm(uniqueId, 1631713185980, false, copy, "test", true, "test", false, false)
        dao.addAlarm(alarm)

        val test = dao.getById(uniqueId)
        assertEquals(test.id, uniqueId)
        assertEquals(test.label, "test")
        assertEquals(test.ringtone, "test")

        val newAlarm = Alarm(
            test.id,
            test.time,
            test.activate,
            test.days,
            "update",
            test.vibrate,
            "update",
            test.displayed,
            test.show
        )
        dao.updateAlarm(newAlarm)

        val newTest = dao.getById(uniqueId)
        assertEquals(newTest.label, "update")
        assertEquals(newTest.ringtone, "update")

        dao.deleteAlarm(newTest)
        val final = dao.getById(uniqueId)
        assertEquals(final, null)
    }
}