package com.sleewell.sleewell.musique

import android.widget.ListAdapter
import com.sleewell.sleewell.mvp.Global.BasePresenter
import com.sleewell.sleewell.mvp.Global.BaseView

interface MainContract {
    interface Model {
        /**
         * This method set the ListAdapter, set all music available in this one
         *
         * @return ListAdapter
         * @author gabin warnier de wailly
         */
        fun setUpAdapterMusique() : ListAdapter
        /**
         * This method launch a music from the ListAdapter set before
         *
         * @param musicInt it's the number of the music from the array list
         * @author gabin warnier de wailly
         */
        fun startMusique(musicInt: Int)
        /**
         * This method stop the current music launch
         *
         * @author gabin warnier de wailly
         */
        fun stopMusique()
        /**
         * This method try to connect to the application Spotify
         *
         * @author gabin warnier de wailly
         */
        fun connectionSpotify()
        /**
         * This method try to disconnect to the application Spotify
         *
         * @/**
         * This method try to play a playlist on Spotify
         *
         * @param idMusic it's the uri, it's similar to the id of a playlist/music/album/...
         *
         * @author gabin warnier de wailly
        */author gabin warnier de wailly
         */
        fun disconnectionSpotify()
        /**
         * This method try to play a playlist on Spotify
         *
         * @param idMusic it's the uri, it's similar to the id of a playlist/music/album/...
         *
         * @return Boolean
         *
         * @author gabin warnier de wailly
         */
        fun playPlaylistSpotify(idMusic: String) : Boolean
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()
        /**
         * this method set up the music and return it
         *
         * @return ListAdapter
         * @author gabin warnier de wailly
         */
        fun getAdapterMusique() : ListAdapter
        /**
         * this method launch the music
         *
         * @param musicInt it's the number of the music from the array list
         * @author gabin warnier de wailly
         */
        fun launchMusique(musicInt: Int)
        /**
         * This method call a model method for the connection to spotify
         *
         * @author gabin warnier de wailly
         */
        fun connectionSpotify()
        /**
         * This method call a model method for the disconnection to spotify
         *
         * @author gabin warnier de wailly
         */
        fun disconnectionSpotify()
        /**
         * This method call a model method for play a music
         *
         * @param idMusic it's the number of the music from the array list
         *
         * @author gabin warnier de wailly
         */
        fun playPlaylistSpotify(idMusic: String)
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
    }
}