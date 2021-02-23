package com.sleewell.sleewell.database.analyse.night

import androidx.room.*
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.database.analyse.night.entities.NightAndAnalyse
import com.sleewell.sleewell.database.analyse.night.entities.NightUpdate

@Dao
interface NightDao {

    @Insert
    suspend fun insertNight(night: Night): Long

    @Insert
    suspend fun insertAll(vararg nights: Night): List<Long>

    @Query("SELECT * FROM night WHERE uId = :id")
    suspend fun getNight(id: Long): Night

    @Query("SELECT * FROM night WHERE start = :timestamp LIMIT 1")
    suspend fun getNightWithTimestamp(timestamp: Long) : Night

    @Transaction
    @Query("SELECT * FROM night WHERE uId = :id")
    suspend fun getNightAnalyse(id: Long): NightAndAnalyse

    @Transaction
    @Query("SELECT * FROM night WHERE start = :timestamp")
    suspend fun getNightAnalyseFromStart(timestamp: Long) : NightAndAnalyse

    @Query("SELECT * FROM night")
    suspend fun getAll(): List<Night>

    @Query("SELECT start FROM night")
    suspend fun getAllStart(): List<Long>

    @Update(entity = Night::class)
    suspend fun updateNightEnd(night: NightUpdate)

    @Delete
    suspend fun deleteAll(vararg nights: Night)

    @Delete
    suspend fun deleteNight(night: Night)
}