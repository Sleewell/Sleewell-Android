package com.sleewell.sleewell.api.sleewell.model

import com.google.gson.annotations.SerializedName

data class ResultRegisterSleewell(var id: String?) {

    @SerializedName("AccessToken")
    var AccessToken: String? = null

    @SerializedName("Success")
    var Success: String? = null
}
