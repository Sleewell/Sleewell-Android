package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IUserApi {
    @GET("user/information")
    fun getProfileInformation(@Header("Authorization") token: String): Call<ProfileInfo>

    @Multipart
    @POST("user/update")
    fun updateProfileInformation(
        @Header("Authorization") token: String,
        @Part("login") username: RequestBody,
        @Part("firstname") firstName: RequestBody,
        @Part("lastname") lastName: RequestBody,
        @Part("email") email: RequestBody,
    ): Call<ResponseSuccess>
}