package com.sleewell.sleewell.database.routine

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.reveil.data.model.Alarm

@Dao
interface RoutineDao {

    @Insert
    fun addNewRoutine(routine : Routine) : Long

    @Query("SELECT * FROM routine WHERE uId = :id")
    fun getRoutine(id: Long): Routine

    @Query("SELECT * FROM routine")
    fun getAllRoutine() : Array<Routine>

    @Update
    fun updateRoutine(routine: Routine)

    @Update
    fun UpdateAllroutine(routines: Array<Routine>)

    @Delete
    fun deleteRoutine(routine: Routine)
}