package com.sleewell.sleewell.database.routine

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sleewell.sleewell.database.routine.entities.Routine

@Database(
    version = 3,
    entities = arrayOf(Routine::class),
    exportSchema = true
)
abstract class RoutineDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var db: RoutineDatabase? = null

        val migration_2_3 : Migration = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE routine ADD COLUMN imagePlaylist TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): RoutineDatabase {
            if (db == null) {
                synchronized(RoutineDatabase) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            context, RoutineDatabase::class.java, "database-routine"
                        ).addMigrations(migration_2_3).build()
                    }
                }
            }
            return db!!
        }
    }
}