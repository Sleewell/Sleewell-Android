package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.ResultLoginSleewell
import com.sleewell.sleewell.api.sleewell.model.ResultRegisterSleewell
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ILoginApi {

    @POST("login")
    fun loginSleewell(@Body file : RequestBody,
    ) : Call<ResultLoginSleewell>

    @POST("oauth/google")
    fun loginGoogle(@Body file : RequestBody,
    ) : Call<ResultLoginSleewell>

    @POST("register")
    fun registerSleewell(@Body file : RequestBody,
    ) : Call<ResultRegisterSleewell>
}