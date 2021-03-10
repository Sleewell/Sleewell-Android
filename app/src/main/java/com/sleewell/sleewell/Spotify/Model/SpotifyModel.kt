package com.sleewell.sleewell.Spotify.Model

import android.content.Context
import android.util.Log
import com.sleewell.sleewell.Spotify.*
import retrofit2.Call
import retrofit2.Callback

class SpotifyModel(context: Context) : MainContract.Model {

    private var context = context
    private var adapter: SpotifyPlaylistAdapter
    private var aList: ArrayList<SpotifyPlaylist> = ArrayList()
    private var api : ApiInterfaceSpotify? = ApiClientSpotify.retrofit.create(ApiInterfaceSpotify::class.java)
    private val TAG = "SpotifyModel"

    init {
        aList.add(SpotifyPlaylist("No playlist found","Try something else", ""))
        adapter = SpotifyPlaylistAdapter(context, aList)
    }

    override fun getPlaylistSpotifySearch(onFinishedListener: MainContract.Model.OnFinishedListener, accessToken : String?, namePlaylist : String) {
        val call : Call<ApiResultSpotify>? = api?.searchPlaylist(namePlaylist, "Bearer $accessToken")

        call?.enqueue(object : Callback<ApiResultSpotify> {

            override fun onResponse(call: Call<ApiResultSpotify>, response: retrofit2.Response<ApiResultSpotify>) {
                val responseRes : ApiResultSpotify? = response.body()

                if (responseRes == null) {
                    Log.e(TAG, "Body null error")
                    Log.e(TAG, "Code : " + response.code())
                    onFinishedListener.onFailure(Throwable("Body null error : " + response.code()))
                } else {
                    Log.e(TAG, "Success")
                    onFinishedListener.onFinished(responseRes)
                }
            }

            override fun onFailure(call: Call<ApiResultSpotify>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }

    override fun updateListPlaylistSpotify(response :ApiResultSpotify) : SpotifyPlaylistAdapter {
        aList.clear()
        for (i in response.playlists!!.items!!.indices) {
            val name = response.playlists!!.items!![i].name
            val uri = response.playlists!!.items!![i].uri
            val image = response.playlists!!.items!![i].images!![0].url
            aList.add(SpotifyPlaylist(name!!, uri!!, image!!))
        }
        if (aList.size == 0) {
            aList.add(SpotifyPlaylist("No playlist found","", ""))
        }
        adapter = SpotifyPlaylistAdapter(context, aList)
        return adapter
    }

    override fun getMusicSelected(index : Int) : SpotifyPlaylist {
        return aList[index]
    }
}