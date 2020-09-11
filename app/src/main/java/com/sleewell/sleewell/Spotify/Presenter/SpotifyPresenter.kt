package com.sleewell.sleewell.Spotify.Presenter

import android.content.Context
import android.text.Editable
import com.sleewell.sleewell.Spotify.MainContract
import com.sleewell.sleewell.Spotify.Model.SpotifyModel
import com.sleewell.sleewell.Spotify.View.SpotifyActivity

class SpotifyPresenter(view: SpotifyActivity, context: Context) : MainContract.Presenter {

    private var view: SpotifyActivity? = view
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
        //model?.getRequest("oui", namePlaylist.toString())
        model?.getAccessToken()
    }
}