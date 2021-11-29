package com.sleewell.sleewell.database

data class tokenData(
    val format: String,
    val data: Data
)

data class Data(
    val spotify: String,
    val google: String
)
