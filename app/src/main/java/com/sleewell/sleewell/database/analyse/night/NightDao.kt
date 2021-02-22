package com.sleewell.sleewell.database.analyse.night

import androidx.room.*

@Dao
interface NightDao {

    @Insert
    suspend fun insertNight(night: Night) : Long

    @Insert
    suspend fun insertAll(vararg nights: Night) : List<Long>

    @Query("SELECT * FROM night WHERE uId = :id")
    suspend fun getNight(id: Long) : Night

    @Transaction
    @Query("SELECT * FROM night WHERE uId = :id")
    suspend fun getNightAnalyse(id: Long): NightAndAnalyse

    @Query("SELECT * FROM night")
    suspend fun getAll() : List<Night>

    @Delete
    suspend fun deleteAll(vararg nights: Night)

    @Delete
    suspend fun deleteNight(night: Night)
}