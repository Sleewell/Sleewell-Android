package com.sleewell.sleewell.musique.View

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.Presenter.MusiquePresenter
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;



/**
 * This class interact with the user, display everything and catch every action of the user
 *
 * @author gabin warnier de wailly
 */
class MusiqueActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private  lateinit var listView: ListView

    private val clientId = "42d9f9b3f8ef4a419766c3f14566f110"
    private val redirectUri = "http://com.sleewell.sleewell/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var spotify_button: Button


    companion object {
        var music_select = 0
    }


    /**
     * This method setup the view
     *
     * @param savedInstanceState creation of the view
     * @author gabin warnier de wailly
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musique)

        setPresenter(MusiquePresenter(this, this))
        InitActivityWidgets()
        presenter.onViewCreated()
    }

    /**
     * This method  setup every widgets created
     *
     * @author gabin warnier de wailly
     */
    private fun InitActivityWidgets() {

        listView = findViewById(R.id.music_list_view)
        listView.adapter = presenter.getAdapterMusique()
        listView.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            presenter.launchMusique(i)
        }

        spotify_button = findViewById(R.id.button_spotify)
        spotify_button.setOnClickListener{
            Launch()
        }
    }

    /**
     * This method save the presenter in the class
     *
     * @param presenter current presenter
     * @author gabin warnier de wailly
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    /**
     * This method is the destructor of the class
     *
     * @author gabin warnier de wailly
     */
    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    fun Launch() {
        Toast.makeText(applicationContext, "Try to connect", Toast.LENGTH_LONG).show()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()



        SpotifyAppRemote.connect(applicationContext, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Toast.makeText(applicationContext, "Fail " + throwable.message, Toast.LENGTH_LONG).show()
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

    }

    private fun connected() {
        Toast.makeText(applicationContext, "Connected", Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        Toast.makeText(applicationContext, "Disconnected", Toast.LENGTH_LONG).show()
    }
}