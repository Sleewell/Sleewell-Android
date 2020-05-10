package com.sleewell.sleewell.halo.Presenter

import android.content.Context
import android.widget.Toast
import com.sleewell.sleewell.halo.MainContract
import com.sleewell.sleewell.halo.Model.HaloModel

class HaloPresenter(view: MainContract.View, context: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private var model: MainContract.Model = HaloModel(context)

    override fun onViewCreated() {
        view?.printHalo(model.getSizeOfCircle())

    }

    override fun onDestroy() {
        view = null
    }

    override fun buttonLess() {
        model.degradesSizeOfCircle()
    }

    override fun buttonMore() {
        model.upgradeSizeOfCircle()
    }
}