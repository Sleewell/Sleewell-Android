package com.sleewell.sleewell.mvp.menu.routine.presenter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sleewell.sleewell.mvp.menu.routine.RoutineContract
import com.sleewell.sleewell.mvp.menu.routine.model.RoutineModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutinePresenter(view: RoutineContract.View, context: Context) : RoutineContract.Presenter {

    private var view: RoutineContract.View? = view
    private var model: RoutineModel = RoutineModel(context)

    override fun onDestroy() {
        view = null
        model.saveRoutineFromList()
    }

    /**
     * this method is not use here
     *
     * @author gabin warnier de wailly
     */
    override fun onViewCreated() {
    }

    override fun createNewItemRoutine(fragmentManager: FragmentManager?, fragment: Fragment) {
        CoroutineScope(Dispatchers.IO).launch {
            model.createNewItemRoutine(fragmentManager, fragment)
        }
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

    override fun updateSpotifyMusicSelected(musicName: String, musicUri: String, musicImage : String, tag: String?) {
        model.updateSpotifyMusicSelected(musicName, musicUri, musicImage, tag)
    }

    override fun updateSleewellMusicSelected(musicName: String, tag: String?) {
        model.updateSleewellMusicSelected(musicName, tag)
    }
}