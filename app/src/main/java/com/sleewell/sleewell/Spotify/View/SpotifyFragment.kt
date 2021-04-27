package com.sleewell.sleewell.Spotify.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.Presenter.SpotifyPresenter
import com.sleewell.sleewell.Spotify.SpotifyPlaylist
import com.sleewell.sleewell.Spotify.SpotifyPlaylistAdapter
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE


class SpotifyFragment: Fragment(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var root: View
    private lateinit var listView: ListView
    private lateinit var editTextSpotify: EditText
    private lateinit var rearchButtonSpotify: Button
    private lateinit var accessToken: String
    private lateinit var musicSelected: SpotifyPlaylist

    private val clientId = "" // /!\ need to hide
    private val redirectUri = "http://com.sleewell.sleewell/callback"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_spotify, container, false)

        setPresenter(SpotifyPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        InitFragmentWidget()
        return root
    }

    private fun InitFragmentWidget() {
        val menu = root.findViewById<Button>(R.id.MusicButton)
        menu.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        editTextSpotify = root.findViewById(R.id.editMusicId)
        rearchButtonSpotify = root.findViewById(R.id.searchSpotifyButton)
        listView = root.findViewById(R.id.playlistSpotifyListView)

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
            fragmentManager?.popBackStack()
        }
        authenticateSpotify()
    }

    override fun getAccessToken() : String {
        if (!MainActivity.getAccessTokenSpotify)
            return ""
        accessToken = MainActivity.accessTokenSpotify
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
        AuthenticationClient.openLoginActivity(context as Activity?, LoginActivity.REQUEST_CODE, request)
    }

    override fun displayPlaylistSpotify(spotifyAdapter: SpotifyPlaylistAdapter) {
        listView.adapter = spotifyAdapter
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        //if (id == android.R.id.home) {
        //    finish()
        //}
        return super.onOptionsItemSelected(item)
    }

    override fun getMusicSelected(): SpotifyPlaylist {
        return musicSelected
    }
}