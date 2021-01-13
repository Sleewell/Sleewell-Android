package com.sleewell.sleewell.mvp.music.model

import android.content.Context
import android.media.MediaPlayer
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.sleewell.sleewell.R

import com.sleewell.sleewell.mvp.music.MainContract
import com.sleewell.sleewell.mvp.music.MusicPlaylist
import com.sleewell.sleewell.mvp.music.MusicPlaylistAdapter
import com.sleewell.sleewell.mvp.music.view.MusicFragment
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

/**
 * This class set up the adapter music and can launch a music and stop it
 *
 * @param context context of the app
 * @author gabin warnier de wailly
 */
class MusicModel(context: Context) : MainContract.Model {

    private var aList: ArrayList<MusicPlaylist> = ArrayList()
    private lateinit var adapter: MusicPlaylistAdapter
    private var context = context
    private var mediaPlayer: MediaPlayer? = null

    private val clientId = "42d9f9b3f8ef4a419766c3f14566f110"
    private val redirectUri = "http://com.sleewell.sleewell/callback"
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

    override fun setUpAdapterMusiqueByName(name: String) : MusicPlaylistAdapter {
        aList = ArrayList()

        val fields = R.raw::class.java.fields
        for (i in fields.indices) {
            val musicInfo = fields[i].name.split("_").toTypedArray()
            if (musicInfo[0] == name || name == "") {
                val title = musicInfo[1]
                val description = "$title sound"
                val image = ""
                val name = fields[i].name
                aList.add(MusicPlaylist(title, description, image, name))
            }
        }
        adapter = MusicPlaylistAdapter(context, aList)
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
        val singh = context.resources.getIdentifier(aList[musicInt].getName(), "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, singh)
        mediaPlayer!!.start()
        MusicFragment.music_select = music_select
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