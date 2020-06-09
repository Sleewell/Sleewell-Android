package com.sleewell.sleewell.musique.Presenter

import android.content.Context
import android.widget.ListAdapter
import com.sleewell.sleewell.musique.MainContract
import com.sleewell.sleewell.musique.Model.MusiqueModel

class MusiquePresenter(view: MainContract.View, context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private var model: MainContract.Model = MusiqueModel(context)


    override fun getAdapterMusique() : ListAdapter {
        return model?.setUpAdapterMusique()
    }

    override fun launchMusique(musicInt: Int) {
        model?.startMusique(musicInt)
    }

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        model?.stopMusique()
        view = null
    }
}