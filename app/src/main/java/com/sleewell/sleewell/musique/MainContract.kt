package com.sleewell.sleewell.musique

import android.widget.ListAdapter
import com.sleewell.sleewell.mvp.Global.BasePresenter
import com.sleewell.sleewell.mvp.Global.BaseView

interface MainContract {
    interface Model {
        fun setUpAdapterMusique() : ListAdapter
        fun startMusique(musicInt: Int)
        fun stopMusique()
    }

    interface Presenter : BasePresenter {
        fun onViewCreated()
        fun getAdapterMusique() : ListAdapter
        fun launchMusique(musicInt: Int)
    }

    interface View : BaseView<Presenter>
}