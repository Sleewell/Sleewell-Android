package com.sleewell.sleewell.mvp.spotify.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.spotify.MainContract
import com.sleewell.sleewell.mvp.spotify.presenter.SpotifyPresenter
import com.sleewell.sleewell.mvp.spotify.SpotifyPlaylist
import com.sleewell.sleewell.mvp.spotify.SpotifyPlaylistAdapter
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import java.lang.ClassCastException


open class SpotifyFragment: DialogFragment(), MainContract.View {

    lateinit var backButton : Button
    private lateinit var presenter: MainContract.Presenter
    private lateinit var root: View
    private lateinit var listView: ListView
    private lateinit var editTextSpotify: EditText
    private lateinit var rearchButtonSpotify: Button
    private lateinit var accessToken: String
    private lateinit var musicSelected: SpotifyPlaylist

    private val clientId = "" // /!\ need to hide
    private val redirectUri = "http://com.sleewell.sleewell/callback"

    var musicName: String = ""
    var musicUri: String = ""
    var musicImage: String = ""

    interface OnInputSelected {
        fun sendInput(musicName : String, musicUri : String, musicImage : String,tag : String?)
    }
    lateinit var selected : OnInputSelected

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.new_fragment_spotify, container, false)

        setPresenter(SpotifyPresenter(this, this.activity as AppCompatActivity))
        presenter.onViewCreated()

        initFragmentWidget()
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            selected = targetFragment as OnInputSelected
        } catch (e : ClassCastException) {
            Log.e("SpotifyFragment", e.message.toString())
        }
    }

    private fun initFragmentWidget() {
        backButton = root.findViewById(R.id.MusicButton)

        backButton.setOnClickListener {
            selected.sendInput(musicName, musicUri, musicImage, tag)
            dismiss()
        }

        editTextSpotify = root.findViewById(R.id.editMusicId)
        rearchButtonSpotify = root.findViewById(R.id.searchSpotifyButton)
        listView = root.findViewById(R.id.playlistSpotifyListView)

        rearchButtonSpotify.setOnClickListener{
            presenter.rearchPlaylist(editTextSpotify.text)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            musicSelected = presenter.getSpotifyMusic(i)

            if (musicSelected.getUri() == "Try something else")
                return@OnItemClickListener

            musicName = musicSelected.getName()
            musicUri = musicSelected.getUri()
            musicImage = musicSelected.getUrlImage()
        }
        authenticateSpotify()
    }

    override fun getAccessToken() : String {
        if (!MainActivity.getAccessTokenSpotify)
            return ""
        accessToken = MainActivity.accessTokenSpotify
        return accessToken
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
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