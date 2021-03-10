package com.sleewell.sleewell.mvp.menu.routine.presenter

import android.content.Context
import android.provider.Contacts
import com.sleewell.sleewell.Spotify.Model.SpotifyModel
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.RoutineListAdapter
import com.sleewell.sleewell.mvp.menu.routine.model.RoutineModel
import kotlinx.coroutines.*

class RoutinePresenter(view: RoutineContract.View, context: Context) : RoutineContract.Presenter {

    private var view: RoutineContract.View? = view
    private var model: RoutineModel = RoutineModel(context)
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

    override fun createNewItemRoutine() {
        CoroutineScope(Dispatchers.IO).launch {
            model.createNewItemRoutine()
        }
    }

    override fun removeItemRoutine(nbr: Int ) {
    }

    override fun updateAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            model.updateListViewRoutine()
        }
        view?.displayRoutineList(model.getAdapter())
    }

    override fun openRoutineDialog(nbr: Int) {
        model.openRoutineParameter(nbr)
    }
}