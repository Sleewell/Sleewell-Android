package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.AddRoutineResponse
import com.sleewell.sleewell.api.sleewell.model.DeleteRoutineResponse
import com.sleewell.sleewell.api.sleewell.model.RoutinesResponse
import com.sleewell.sleewell.api.sleewell.model.UpdateRoutineResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface IRoutineApi {

	@GET("routines")
	fun getRoutines(
		@Header("Authorization") token: String
	): Call<RoutinesResponse>

	@POST("deleteRoutine")
	fun deleteRoutine(
		@Header("Authorization") token: String,
		@Body file : RequestBody,
	): Call<DeleteRoutineResponse>

	@POST("updateRoutine")
	fun updateRoutine(
		@Header("Authorization") token: String,
		@Body file : RequestBody,
	): Call<UpdateRoutineResponse>

	@POST("addRoutine")
	fun addRoutine(
		@Header("Authorization") token: String,
		@Body file : RequestBody,
	): Call<AddRoutineResponse>
}