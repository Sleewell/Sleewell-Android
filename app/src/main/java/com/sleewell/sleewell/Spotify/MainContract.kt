package com.sleewell.sleewell.Spotify

import android.text.Editable
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface MainContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(weather : ApiResultSpotify)
            fun onFailure(t : Throwable)
        }
        fun updateListPlaylistSpotify(response :ApiResultSpotify) : SpotifyPlaylistAdapter
        fun getMusicSelected(index : Int) : SpotifyPlaylist
        fun getPlaylistSpotifySearch(onFinishedListener: OnFinishedListener, accessToken: String?, namePlaylist: String)
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()

        fun rearchPlaylist(namePlaylist: Editable)

        fun getSpotifyMusic(index : Int) : SpotifyPlaylist
    }

    interface View : BaseView<Presenter> {
        /**
         * This method will display the message give in param
         *
         * @param message message how will be display
         *
         * @author gabin warnier de wailly
         */
        fun displayToast(message: String)

        fun getAccessToken() : String

        fun getMusicSelected() : SpotifyPlaylist

        fun displayPlaylistSpotify(spotifyAdapter: SpotifyPlaylistAdapter)
    }
}