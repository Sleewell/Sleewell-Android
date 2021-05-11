package com.sleewell.sleewell

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sleewell.sleewell.reveil.data.AlarmDao
import com.sleewell.sleewell.reveil.data.AlarmDatabase
import com.sleewell.sleewell.reveil.data.model.Alarm
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class POOPSHOOP222Test {
/*    private lateinit var alarmDao: AlarmDao
    private lateinit var db: AlarmDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AlarmDatabase::class.java).build()
        alarmDao = db.alarmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun writeUserAndReadInList() {
        val alarm = Alarm(1000, 1, false, listOf(false), "tmp", false, "TEST", true)
        alarmDao.addAlarm(alarm)
        val byId = alarmDao.getById(1000)
        assertEquals(byId.label, "TEST")
    }*/
}