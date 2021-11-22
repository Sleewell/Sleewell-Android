package com.sleewell.sleewell.api.sleewell.model

import com.google.gson.annotations.SerializedName

data class ResultLoginSleewell(var id: String?) {

    @SerializedName("AccessToken")
    var accessToken: String? = null

    @SerializedName("Success")
    var success: String? = null

    @SerializedName("is_validate")
    var isValidate: Boolean? = null
}
