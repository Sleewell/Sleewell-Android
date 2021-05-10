package com.sleewell.sleewell.database.analyse.night

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sleewell.sleewell.database.analyse.night.entities.Analyse
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

@Dao
interface AnalyseDao {

    @Insert
    suspend fun insertAnalyse(analyse: Analyse): Long

    @Insert
    suspend fun insertAll(vararg analyses: Analyse): List<Long>

    @Query("SELECT db, ts FROM analyse INNER JOIN night ON night.start = :timestamp WHERE analyse.nightId = night.uId")
    suspend fun getAnalyseFromNightStart(timestamp: Long): List<AnalyseValue>

    @Query("SELECT * FROM analyse WHERE nightId = :nightId")
    suspend fun getAnalysesFromNightId(nightId: Long): List<Analyse>

    @Query("SELECT * FROM analyse WHERE uId = :id")
    suspend fun getAnalyse(id: Long): Analyse

    @Query("SELECT * FROM analyse")
    suspend fun getAll(): List<Analyse>

    @Query("DELETE FROM analyse WHERE nightId = :nightId")
    suspend fun deleteAnalyseFromNightId(nightId: Long)

    @Delete
    suspend fun deleteAnalyse(analyse: Analyse)

    @Delete
    suspend fun deleteAll(vararg analyses: Analyse)
}