package com.sleewell.sleewell.reveil.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.model.DataConverter

@Database(entities = [Alarm::class], version = 4, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AlarmDatabase: RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        /**
         * Get the database of the alarms.
         *
         * @param context Context of the application.
         * @return The database.
         * @author Romane Bézier
         */
        fun getDatabase(context: Context): AlarmDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}