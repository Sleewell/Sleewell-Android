package com.sleewell.sleewell.mvp.spotify

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterfaceSpotify {

    @GET("search")
    fun searchPlaylist(@Query("q") playlistName: String,
                       @Header("Authorization") token: String,
                       @Header("Accept") accept: String = "application/json",
                       @Header("Content-Type") content: String = "application/json",
                       @Query("type") type : String = "playlist",
                       @Query("limit") limit : String = "10",
                       @Query("offset") offset : String = "0"

    ) : Call<ApiResultSpotify>
}