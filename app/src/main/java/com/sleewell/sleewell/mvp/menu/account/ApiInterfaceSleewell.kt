package com.sleewell.sleewell.mvp.menu.account

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterfaceSleewell {

    @POST("login")
    fun loginSleewell(@Body file : RequestBody,
    ) : Call<ApiResultLoginSleewell>

    @POST("register")
    fun registerSleewell(@Body file : RequestBody,
    ) : Call<ApiResultRegisterSleewell>
}