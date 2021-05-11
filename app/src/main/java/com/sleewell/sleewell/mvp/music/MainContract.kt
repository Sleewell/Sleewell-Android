package com.sleewell.sleewell.mvp.music

import android.widget.ListAdapter
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface MainContract {
    interface Model {

        fun setUpAdapterMusiqueByName(name: String) : MusicPlaylistAdapter
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
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()
        /**
         * this method launch the music
         *
         * @param name it's the frist name of the music from the menu
         * @author gabin warnier de wailly
         */
        fun getAdapterMusiqueByName(name: String) : MusicPlaylistAdapter
        /**
         * this method launch the music
         *
         * @param musicInt it's the number of the music from the array list
         * @author gabin warnier de wailly
         */
        fun launchMusique(musicInt: Int)

        /**
         * this method stop the music
         *
         * @author gabin warnier de wailly
         */
        fun stopMusique()
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