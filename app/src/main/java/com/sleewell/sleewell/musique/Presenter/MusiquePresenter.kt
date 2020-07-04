package com.sleewell.sleewell.musique.Presenter

import android.content.Context
import android.widget.ListAdapter
import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.Model.MusiqueModel

/**
 * this class can control the music
 *
 * @param view
 * @param context
 * @author gabin warnier de wailly
 */
class MusiquePresenter(view: MainContract.View, context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private var model: MainContract.Model = MusiqueModel(context)


    /**
     * this method set up the music and return it
     *
     * @return ListAdapter
     * @author gabin warnier de wailly
     */
    override fun getAdapterMusique() : ListAdapter {
        return model?.setUpAdapterMusique()
    }

    /**
     * this method launch the music
     *
     * @param musicInt it's the number of the music from the array list
     * @author gabin warnier de wailly
     */
    override fun launchMusique(musicInt: Int) {
        model?.startMusique(musicInt)
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
        model?.stopMusique()
        view = null
    }
}