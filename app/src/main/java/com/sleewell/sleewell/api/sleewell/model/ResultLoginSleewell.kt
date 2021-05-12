package com.sleewell.sleewell.api.sleewell.model

import com.google.gson.annotations.SerializedName

data class ResultLoginSleewell(var id: String?) {

    @SerializedName("AccessToken")
    var AccessToken: String? = null

    @SerializedName("Success")
    var Success: String? = null

    @SerializedName("is_validate")
    var is_validate: Boolean? = null
}
