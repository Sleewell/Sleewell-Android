package com.sleewell.sleewell.api.sleewell.model

data class RoutinesResponse(
	val data: Array<Routine>?,
)

data class Routine(
	val id: Int,
	val color: String,
	val halo: Int,
	val duration: Int,
	val usemusic: Int,
	val musicName: String,
	val musicUri: String,
	val player: String,
	val name: String,
)
