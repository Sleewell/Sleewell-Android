package com.sleewell.sleewell.mvp.menu.account

import com.google.gson.annotations.SerializedName

data class ApiResultRegisterSleewell(var id: String?) {

    @SerializedName("AccessToken")
    var AccessToken: String? = null

    @SerializedName("Success")
    var Success: String? = null
}
