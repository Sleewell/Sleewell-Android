package com.sleewell.sleewell.api.sleewell

import com.sleewell.sleewell.api.sleewell.model.profile.ProfileInfo
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseBody
import com.sleewell.sleewell.api.sleewell.model.profile.ResponseSuccess
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart

interface IUserApi {
    @GET("user/information")
    fun getProfileInformation(
        @Header("Authorization") token: String
    ): Call<ProfileInfo>

    @Multipart
    @POST("user/update")
    fun updateProfileInformation(
        @Header("Authorization") token: String,
        @Part("login") username: RequestBody,
        @Part("firstname") firstName: RequestBody,
        @Part("lastname") lastName: RequestBody,
        @Part("email") email: RequestBody,
    ): Call<ResponseSuccess>

    @GET("user/get-picture")
    fun getProfilePicture(
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    @Multipart
    @POST("user/picture")
    suspend fun uploadProfilePicture(
        @Header("Authorization") token: String,
        @Part body: MultipartBody.Part
    ): ResponseSuccess

    @POST("user/delete")
    fun deleteAccount(
        @Header("Authorization") token: String
    ): Call<ResponseSuccess>
}