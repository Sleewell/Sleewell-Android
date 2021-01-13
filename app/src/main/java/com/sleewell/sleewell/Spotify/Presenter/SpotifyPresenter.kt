package com.sleewell.sleewell.Spotify.Presenter

import android.content.Context
import android.text.Editable
import com.sleewell.sleewell.Spotify.ApiResultSpotify
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.Model.SpotifyModel
import com.sleewell.sleewell.Spotify.SpotifyPlaylist
import com.sleewell.sleewell.Spotify.View.SpotifyActivity
import com.sleewell.sleewell.Spotify.View.SpotifyFragment

class SpotifyPresenter(view: SpotifyFragment, context: Context) : MainContract.Presenter,
    MainContract.Model.OnFinishedListener {

    private var view: SpotifyFragment? = view
    private var model: SpotifyModel = SpotifyModel(context)
    private var context = context

    /**
     * this method is not use here
     *
     * @author gabin warnier de wailly
     */
    override fun onViewCreated() {
    }

    /**
     * This method is the destructor of the class and stop the current music
     *
     * @author gabin warnier de wailly
     */
    override fun onDestroy() {
        view = null
    }

    override fun rearchPlaylist(namePlaylist: Editable) {
        if (namePlaylist.toString() == "") {
            view!!.displayToast("Enter a name of playlist")
        } else {
            val accessToken = view?.getAccessToken()
            if (accessToken!!.isNotEmpty())
                model.getPlaylistSpotifySearch(this, view?.getAccessToken(), namePlaylist.toString())
        }
    }

    override fun getSpotifyMusic(index : Int) : SpotifyPlaylist {
        return model.getMusicSelected(index)
    }

    override fun onFinished(playlist: ApiResultSpotify) {
        val adapter = model.updateListPlaylistSpotify(playlist)
        view?.displayPlaylistSpotify(adapter)
    }

    override fun onFailure(t: Throwable) {
        if (t.message != null)
            view?.displayToast(t.message!!)
        else
            view?.displayToast("An error occurred")
    }
}