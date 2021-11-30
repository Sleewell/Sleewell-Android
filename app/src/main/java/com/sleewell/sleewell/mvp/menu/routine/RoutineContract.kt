package com.sleewell.sleewell.mvp.menu.routine

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface RoutineContract {

    interface Model {
        /**
         * This method create a new routine and add it in database directly
         *
         * @author gabin warnier de wailly
         */
        fun createNewItemRoutine(fragmentManager: FragmentManager?, fragment: Fragment)

        /**
         * This method return the adapter for the listview (routine)
         *
         * @return an adapter form RoutineListAdapter
         *
         * @author gabin warnier de wailly
         */
        fun getAdapter() : RoutineListAdapter

        /**
         * This method remove an item in the routine database
         *
         * @param routine the routine you want delete
         *
         * @author gabin warnier de wailly
         */
        fun removeNewItemRoutine(routine: Routine)


        /**
         * This method update a routine in the database
         *
         * @param routine the routine you want update
         * @param nbr the index in the ListView
         *
         * @author gabin warnier de wailly
         */
        fun updateItemRoutine(routine: Routine, nbr: Int)

        /**
         * This method update in the routine the music you selected from Spotify
         *
         * @param musicName name of the music (Spotify)
         * @param musicUri uri of the music (Spotify)
         * @param tag Uid of the routine from database
         *
         * @author gabin warnier de wailly
         */
        fun updateSpotifyMusicSelected(musicName: String, musicUri: String, musicImage: String, tag: String?)

        /**
         * This method update in the routine the music you selected from Sleewell
         *
         * @param musicName name of the music (Sleewell)
         * @param tag Uid of the routine from database
         *
         * @author gabin warnier de wailly
         */
        fun updateSleewellMusicSelected(musicName: String, tag: String?)

        /**
         * This method open the fragment form more detail of a routine
         *
         * @param nbr index of the routine
         * @param fragmentManager fragment manager will be use for open music selector (Spotify or Sleewell)
         * @param fragment fragment will be use for open music selector (Spotify or Sleewell)
         *
         *  @author gabin warnier de wailly
         */
        fun openRoutineParameter(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment)

        /**
         * This method will set the routine selected at the state selected and reset all other to unselected
         *
         * @param nbr index in list
         *
         * @author gabin warnier de wailly
         */
        fun updateSelectedItemRoutine(nbr: Int)
		fun getRoutineApiSleewell()
        fun updateListViewOffLine()
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()

        /**
         * this method create a new routine
         *
         * @author gabin warnier de wailly
         */
        fun createNewItemRoutine(fragmentManager: FragmentManager?, fragment: Fragment)

        /**
         * This method remove a routine
         *
         * @param nbr index in list of the routine
         *
         * @author gabin warnier de wailly
         */
        fun removeItemRoutine(nbr: Int)

        /**
         * This method update the adapter
         *
         * @author gabin warnier de wailly
         */
        fun updateAdapter()

        /**
         * This method open the fragment form more detail of a routine
         *
         * @param nbr index of the routine
         * @param fragmentManager fragment manager will be use for open music selector (Spotify or Sleewell)
         * @param fragment fragment will be use for open music selector (Spotify or Sleewell)
         *
         * @author gabin warnier de wailly
         */
        fun openRoutineDialog(nbr: Int, fragmentManager: FragmentManager?, fragment: Fragment)

        /**
         * This method update in the routine the music you selected from Spotify
         *
         * @param musicName name of the music (Spotify)
         * @param musicUri uri of the music (Spotify)
         * @param tag Uid of the routine from database
         *
         * @author gabin warnier de wailly
         */
        fun updateSpotifyMusicSelected(musicName: String, musicUri: String, musicImage : String,tag: String?)

        /**
         * This method update in the routine the music you selected from Sleewell
         *
         * @param musicName name of the music (Sleewell)
         * @param tag Uid of the routine from database
         *
         * @author gabin warnier de wailly
         */
        fun updateSleewellMusicSelected(musicName: String, tag: String?)
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

        /**
         * THis method set the routine adapter
         *
         * @param routineAdapter from the database
         *
         * @author gabin warnier de wailly
         */
        fun displayRoutineList(routineAdapter: RoutineListAdapter)
    }
}