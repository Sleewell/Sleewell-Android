package com.sleewell.sleewell.musique.Model

import android.content.Context
import android.media.MediaPlayer
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.sleewell.sleewell.R

import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.View.MusiqueActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

/**
 * This class set up the adapter music and can launch a music and stop it
 *
 * @param context context of the app
 * @author gabin warnier de wailly
 */
class MusiqueModel(context: Context) : MainContract.Model {

    private lateinit var list: MutableList<String>
    private lateinit var adapter: ListAdapter
    private var context = context
    private var mediaPlayer: MediaPlayer? = null

    private val clientId = ""
    private val redirectUri = ""
    private var spotifyAppRemote: SpotifyAppRemote? = null

    companion object {
        var music_select = 0
    }

    /**
     * This method set the ListAdapter, set all music available in this one
     *
     * @return ListAdapter
     * @author gabin warnier de wailly
     */
    override fun setUpAdapterMusique() : ListAdapter {
        list = ArrayList()

        val fields = R.raw::class.java.fields
        for (i in fields.indices) {
            list.add(fields[i].name)
        }
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
        return adapter
    }

    /**
     * This method launch a music from the ListAdapter set before
     *
     * @param musicInt it's the number of the music from the array list
     * @author gabin warnier de wailly
     */
    override fun startMusique(musicInt: Int) {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        val singh = context.resources.getIdentifier(list[musicInt], "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, singh)
        mediaPlayer!!.start()
        MusiqueActivity.music_select = music_select
    }

    /**
     * This method stop the current music launch
     *
     * @author gabin warnier de wailly
     */
    override fun stopMusique() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }

    /**
     * This method try to connect to the application Spotify
     *
     * @author gabin warnier de wailly
     */
    override fun connectionSpotify() {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {

            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Toast.makeText(context, "Fail " + throwable.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * This method try to disconnect to the application Spotify
     *
     * @author gabin warnier de wailly
     */
    override fun disconnectionSpotify() {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }


    /**
     * this method will manage after the connection on spotify
     *
     */
    private fun connected() {
        Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
    }

    /**
     * This method try to play a playlist on Spotify
     *
     * @param idMusic it's the uri, it's similar to the id of a playlist/music/album/...
     *
     * @return Boolean
     *
     * @author gabin warnier de wailly
     */
    override fun playPlaylistSpotify(idMusic: String) : Boolean {
        stopMusique()
        spotifyAppRemote?.let {
            if (spotifyAppRemote?.isConnected!!) {
                spotifyAppRemote?.playerApi?.play(idMusic)
                return true
            }
        }
        return false
    }
}