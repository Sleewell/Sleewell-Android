package com.sleewell.sleewell.Spotify

import android.text.Editable
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import com.sleewell.sleewell.mvp.Global.BasePresenter
import com.sleewell.sleewell.mvp.Global.BaseView

interface MainContract {
    interface Model {
        fun researchPlaylistSpotify(namePlaylist : String, accessToken : String?)
        fun updateListPlaylistSpotify() : SpotifyPlaylistAdapter
        fun getMusicSelected(index : Int) : SpotifyPlaylist
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()

        fun rearchPlaylist(namePlaylist: Editable)

        fun updateListPlaylistSpotify() : SpotifyPlaylistAdapter

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
    }
}