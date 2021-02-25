package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.NighAnalyse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ISleewellApi {
    @POST("stat/post-stat")
    fun postStat(@Body night: NighAnalyse) : Call<String>
}