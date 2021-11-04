package com.sleewell.sleewell.api.sleewell.model.profile

import com.google.gson.annotations.SerializedName

data class ResponseBody(
    @SerializedName("file_path")
    val filePath: String
)
