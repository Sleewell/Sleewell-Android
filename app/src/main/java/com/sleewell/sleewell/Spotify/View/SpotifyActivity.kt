package com.sleewell.sleewell.Spotify.View

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.Presenter.SpotifyPresenter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_playlist_spotify)
        setPresenter(SpotifyPresenter(this, this))
        presenter.onViewCreated()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextSpotify = findViewById(R.id.editMusicId)
        rearchButtonSpotify = findViewById(R.id.rearchSpotifyButton)
        rearchButtonSpotify.setOnClickListener{ presenter.rearchPlaylist(editTextSpotify.text) }

        authenticateSpotify()
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


    private val clientId = "42d9f9b3f8ef4a419766c3f14566f110"
    private val redirectUri = "http://com.sleewell.sleewell/callback"

    fun authenticateSpotify() {
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

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response =
                AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    displayToast(response.accessToken)
                }
                AuthenticationResponse.Type.ERROR -> {
                    displayToast("NONN")
                }
                else -> {
                }
            }
        }
    }

}