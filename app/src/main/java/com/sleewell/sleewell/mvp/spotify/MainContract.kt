package com.sleewell.sleewell.mvp.spotify

import android.text.Editable
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface MainContract {
    interface Model {
        interface OnFinishedListener {
            /**
             * This method is call when the request is finish
             *
             * @param weather result for spotify
             *
             * @author gabin warnier de wailly
             */
            fun onFinished(weather : ApiResultSpotify)
            /**
             * This method is call when the request fail
             *
             * @param t error information
             *
             * @author gabin warnier de wailly
             */
            fun onFailure(t : Throwable)
        }
        /**
         * This method will update the playlist spotify
         *
         * @param response response of spotify request
         *
         * @return SpotifyPlaylistAdapter
         * @author gabin warnier de wailly
         */
        fun updateListPlaylistSpotify(response :ApiResultSpotify) : SpotifyPlaylistAdapter
        /**
         * This method will return the music information
         *
         * @param index the number of music selected
         *
         * @return music information
         * @author gabin warnier de wailly
         */
        fun getMusicSelected(index : Int) : SpotifyPlaylist
        /**
         * This method will send a request for the search of playlist on spotify
         *
         * @param onFinishedListener listener when it is finish
         * @param accessToken token for spotify
         * @param namePlaylist name of the playlist
         *
         * @author gabin warnier de wailly
         */
        fun getPlaylistSpotifySearch(onFinishedListener: OnFinishedListener, accessToken: String?, namePlaylist: String)
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()

        /**
         * This method will call model method for search a playlist on spotify
         *
         * @param namePlaylist the search did for playlist
         *
         * @author gabin warnier de wailly
         */
        fun rearchPlaylist(namePlaylist: Editable)

        /**
         * This method will get the spotify playlist information
         *
         * @param index number music
         *
         * @author gabin warnier de wailly
         */
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

        /**
         * This method will give you the AccessToken for Spotify
         *
         * @return String return "" if error
         *
         * @author gabin warnier de wailly
         */
        fun getAccessToken() : String

        /**
         * This method return the Spotify paylist information
         *
         * @return SpotifyPlaylist
         * @author gabin warnier de wailly
         */
        fun getMusicSelected() : SpotifyPlaylist

        /**
         * This method display in the ListView playlist spotify information
         *
         * @param spotifyAdapter spotify adapter for ListView
         * @author gabin warnier de wailly
         */
        fun displayPlaylistSpotify(spotifyAdapter: SpotifyPlaylistAdapter)
    }
}