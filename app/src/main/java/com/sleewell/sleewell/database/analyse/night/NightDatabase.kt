package com.sleewell.sleewell.database.analyse.night

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sleewell.sleewell.database.analyse.night.entities.Analyse
import com.sleewell.sleewell.database.analyse.night.entities.Night

@Database(
    version = 2,
    entities = arrayOf(Night::class, Analyse::class),
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class NightDatabase : RoomDatabase() {
    abstract fun nightDao(): NightDao

    abstract fun analyseDao(): AnalyseDao

    companion object {
        @Volatile
        private var db: NightDatabase? = null

        fun getDatabase(context: Context): NightDatabase {
            if (db == null) {
                synchronized(NightDatabase) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            context,
                            NightDatabase::class.java,
                            "database-night-analyse"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return db!!
        }
    }
}