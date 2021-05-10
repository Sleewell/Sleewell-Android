package com.sleewell.sleewell.database.routine

import androidx.room.*
import com.sleewell.sleewell.database.routine.entities.Routine

@Dao
interface RoutineDao {

    @Insert
    fun addNewRoutine(routine : Routine) : Long

    @Query("SELECT * FROM routine WHERE uId = :id")
    fun getRoutine(id: Long): Routine

    @Query("SELECT * FROM routine WHERE isSelected = 1")
    fun getRoutineSelected() : Array<Routine>

    @Query("SELECT * FROM routine")
    fun getAllRoutine() : Array<Routine>

    @Update
    fun updateRoutine(routine: Routine)

    @Update
    fun UpdateAllroutine(routines: Array<Routine>)

    @Delete
    fun deleteRoutine(routine: Routine)
}