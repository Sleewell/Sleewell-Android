package com.sleewell.sleewell.Spotify.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.Presenter.SpotifyPresenter
import com.sleewell.sleewell.Spotify.SpotifyPlaylist
import com.sleewell.sleewell.Spotify.SpotifyPlaylistAdapter
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE


class SpotifyActivity :  AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var listView: ListView
    private lateinit var editTextSpotify: EditText
    private lateinit var rearchButtonSpotify: Button
    private lateinit var accessToken: String
    private lateinit var musicSelected: SpotifyPlaylist

    private val clientId = "NEED TO FILL" // /!\ need to hide
    private val redirectUri = "http://com.sleewell.sleewell/callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_playlist_spotify)
        setPresenter(SpotifyPresenter(this, this))
        presenter.onViewCreated()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextSpotify = findViewById(R.id.editMusicId)
        rearchButtonSpotify = findViewById(R.id.rearchSpotifyButton)
        listView = findViewById(R.id.playlistSpotifyListView)

        rearchButtonSpotify.setOnClickListener{
            presenter.rearchPlaylist(editTextSpotify.text)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            val resultIntent = Intent()

            musicSelected = presenter.getSpotifyMusic(i)

            if (musicSelected.getUri() == "Try something else")
                return@OnItemClickListener
            resultIntent.putExtra("nameMusicSelected", musicSelected.getName())
            resultIntent.putExtra("uriMusicSelected", musicSelected.getUri())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        authenticateSpotify()
    }

    override fun getAccessToken() : String {
        return accessToken
    }

    private fun authenticateSpotify() {
        val builder = AuthenticationRequest.Builder(
            clientId,
            AuthenticationResponse.Type.TOKEN,
            redirectUri
        )
        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        AuthenticationClient.openLoginActivity(this, LoginActivity.REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response =
                AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    displayToast("Connected to Spotify")
                    accessToken = response.accessToken
                }
                AuthenticationResponse.Type.ERROR -> {
                    displayToast("Connection to Spotify Failed")
                }
                else -> {
                }
            }
        }
    }

    override fun displayPlaylistSpotify(spotifyAdapter: SpotifyPlaylistAdapter) {
        listView.adapter = spotifyAdapter
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getMusicSelected(): SpotifyPlaylist {
        return musicSelected
    }
}