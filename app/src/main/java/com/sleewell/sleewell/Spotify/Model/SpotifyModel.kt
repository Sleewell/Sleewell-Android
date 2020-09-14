package com.sleewell.sleewell.Spotify.Model

import android.content.Context
import android.widget.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.SpotifyPlaylist
import com.sleewell.sleewell.Spotify.SpotifyPlaylistAdapter
import org.json.JSONException
import org.json.JSONObject

class SpotifyModel(context: Context) : MainContract.Model {

    private var context = context
    private var queue: RequestQueue? = null
    private lateinit var adapter: SpotifyPlaylistAdapter
    private var aList: ArrayList<SpotifyPlaylist> = ArrayList()

    init {
        aList.add(SpotifyPlaylist("No playlist found","Try something else", ""))
        adapter = SpotifyPlaylistAdapter(context, aList)
    }

    override fun researchPlaylistSpotify(namePlaylist : String, accessToken : String?) {
        queue = Volley.newRequestQueue(context)

        val request = object : JsonObjectRequest(
            Method.GET, "https://api.spotify.com/v1/search?q=" + namePlaylist +  "&type=playlist&limit=10&offset=0", null,
            Response.Listener { response ->try {
                    aList.clear()
                    fillPlaylist(response)
                    adapter = SpotifyPlaylistAdapter(context, aList)
                    Toast.makeText(context, "Refresh ready", Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer " + accessToken
                return headers
            }
        }
        queue?.add(request)
    }

    override fun updateListPlaylistSpotify() : SpotifyPlaylistAdapter {
        return adapter
    }

    override fun getMusicSelected(index : Int) : SpotifyPlaylist {
        return aList[index]
    }

    fun fillPlaylist(response :JSONObject) {
        val jsonObject = response.getJSONObject("playlists")
        val items = jsonObject.getJSONArray("items")
        for (i in 0 until items.length()) {
            val playlist = items.getJSONObject(i)
            val name = playlist.getString("name")
            val uri = playlist.getString("uri")
            val images = playlist.getJSONArray("images")
            val image = images.getJSONObject(0).getString("url")

            aList.add(SpotifyPlaylist(name, uri, image))
        }
        if (aList.size == 0) {
            aList.add(SpotifyPlaylist("No playlist found","", ""))
        }
    }
}