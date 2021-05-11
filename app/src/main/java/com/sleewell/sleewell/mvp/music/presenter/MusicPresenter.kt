package com.sleewell.sleewell.mvp.music.presenter

import android.content.Context
import android.widget.ListAdapter
import com.sleewell.sleewell.mvp.music.MainContract
import com.sleewell.sleewell.mvp.music.MusicPlaylistAdapter
import com.sleewell.sleewell.mvp.music.model.MusicModel


/**
 * this class can control the music
 *
 * @param view
 * @param context
 * @author gabin warnier de wailly
 */
class MusicPresenter(view: MainContract.View, context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private var model: MainContract.Model = MusicModel(context)

    override fun getAdapterMusiqueByName(name: String) : MusicPlaylistAdapter {
        return model.setUpAdapterMusiqueByName(name)
    }

    /**
     * this method launch the music
     *
     * @param musicInt it's the number of the music from the array list
     * @author gabin warnier de wailly
     */
    override fun launchMusique(musicInt: Int) {
        model.startMusique(musicInt)
    }

    override fun stopMusique() {
        model.stopMusique()
    }

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
        model.stopMusique()
        view = null
    }
}