package com.sleewell.sleewell.mvp.menu.routine.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract

class RoutinePresenter(view: RoutineContract.View, context: Context) : RoutineContract.Presenter {

    private var view: RoutineContract.View? = view
    private var context = context

    override fun onDestroy() {
        view = null
    }

    /**
     * this method is not use here
     *
     * @author gabin warnier de wailly
     */
    override fun onViewCreated() {
    }
}