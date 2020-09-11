package com.sleewell.sleewell.Spotify.Model

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sleewell.sleewell.Spotify.MainContract
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE


class SpotifyModel(context: Context) : MainContract.Model {

    private var context = context
    private var queue: RequestQueue? = null


    fun getAccessToken() {
    }

    fun getRequest(url : String, music : String) {

        queue = Volley.newRequestQueue(context)

        val request = object : JsonObjectRequest(
            Request.Method.GET, "https://api.spotify.com/v1/search?q=oui&type=playlist&limit=2&offset=0", null,
            Response.Listener { response ->
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
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
                headers["Authorization"] = "Bearer BQBmWzHX3Z7XmI8Ssvo9IWcfQz9kDg-STGvJ-ZEauOWTUkod-ZyWDRYzEQez0sNj_v3MO9uIYb5xLZNz2_aTszeX7np_xE82j7pUZsB9fpApdf_wyl5lW7olGKX7ot-txjYLwBaXfLNnnBqNMymmKySppKFtXrN6r74"
                return headers
            }
        }

        queue?.add(request)
    }
}

// https://accounts.spotify.com/authorize
// ?client_id= 5fe01282e44241328a84e7c5cc169165
// &response_type=code
// &redirect_uri=http%3A%2F%2Fcom.sleewell.sleewell.com%2Fcallback
// &scope=user-read-private%20user-read-email&state=34fFs29kd09



//http://com.sleewell.sleewell/callback