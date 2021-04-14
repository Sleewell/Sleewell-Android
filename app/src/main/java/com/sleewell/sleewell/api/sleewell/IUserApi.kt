package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.ResponseSuccess
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IUserApi {
    @Multipart
    @POST("user/information")
    fun getProfileInformation(@Part("token") token: RequestBody): Call<ProfileInfo>

    @Multipart
    @POST("user/update")
    fun updateProfileInformation(
        @Part("token") token: RequestBody,
        @Part("login") username: RequestBody,
        @Part("firstname") firstName: RequestBody,
        @Part("lastname") lastName: RequestBody
    ): Call<ResponseSuccess>
}