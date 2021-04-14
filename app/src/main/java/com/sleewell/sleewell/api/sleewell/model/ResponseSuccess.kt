package com.sleewell.sleewell.api.sleewell.model

import com.google.gson.annotations.SerializedName

data class ResponseSuccess(
    @SerializedName("Success")
    val success: String
)
