package com.sleewell.sleewell.database.routine

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sleewell.sleewell.database.routine.entities.Routine

@Database(entities = arrayOf(Routine::class), exportSchema = false, version = 1)
abstract class RoutineDatabase : RoomDatabase() {

    abstract fun routineDao() : RoutineDao

    companion object {
        @Volatile
        private var db : RoutineDatabase? = null

        fun getDatabase(context: Context) : RoutineDatabase {
            if (db == null) {
                synchronized(RoutineDatabase) {
                    if (db == null) {
                        db = Room.databaseBuilder(context, RoutineDatabase::class.java, "database-routine").build()
                    }
                }
            }
            return db!!
        }
    }
}