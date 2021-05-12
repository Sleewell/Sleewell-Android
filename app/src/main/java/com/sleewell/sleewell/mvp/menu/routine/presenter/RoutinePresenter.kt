package com.sleewell.sleewell.mvp.menu.routine.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
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
        val rt = Routine("", false, 0, 48, 63, 159, false, 48, false, "None", "", "")
        model.addRoutineApiSleewell(rt)
    }

    override fun removeItemRoutine(nbr: Int ) {
    }

    override fun updateAdapter() {
        model.getRoutineApiSleewell()
        view?.displayRoutineList(model.getAdapter())
    }

    override fun openRoutineDialog(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment) {
        model.openRoutineParameter(nbr, fragmentManager, fragment)
    }

    override fun updateSpotifyMusicSelected(musicName: String, musicUri: String, tag: String?) {
        model.updateSpotifyMusicSelected(musicName, musicUri, tag)
    }

    override fun updateSleewellMusicSelected(musicName: String, tag: String?) {
        model.updateSleewellMusicSelected(musicName, tag)
    }
}