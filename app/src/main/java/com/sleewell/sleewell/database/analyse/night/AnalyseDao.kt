package com.sleewell.sleewell.database.analyse.night

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnalyseDao {

    @Insert
    suspend fun insertAnalyse(analyse : Analyse) : Long

    @Insert
    suspend fun insertAll(vararg analyses : Analyse) : List<Long>

    @Query("SELECT * FROM analyse WHERE nightId = :nightId")
    suspend fun getAnalysesFromNightId(nightId: Long) : List<Analyse>

    @Query("SELECT * FROM analyse WHERE uId = :id")
    suspend fun getAnalyse(id: Long) : Analyse

    @Query("SELECT * FROM analyse")
    suspend fun getAll() : List<Analyse>

    @Delete
    suspend fun deleteAnalyse(analyse: Analyse)

    @Delete
    suspend fun deleteAll(vararg analyses: Analyse)
}