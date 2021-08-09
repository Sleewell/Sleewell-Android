package com.sleewell.sleewell.database.routine

import android.content.Context
import androidx.room.Database
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sleewell.sleewell.database.routine.entities.Routine

@Database(
    version = 2,
    entities = arrayOf(Routine::class),
    /*autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],*/
    exportSchema = true
)
abstract class RoutineDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var db: RoutineDatabase? = null

        fun getDatabase(context: Context): RoutineDatabase {
            if (db == null) {
                synchronized(RoutineDatabase) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            context,
                            RoutineDatabase::class.java,
                            "database-routine"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return db!!
        }
    }
}