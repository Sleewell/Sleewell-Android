package com.sleewell.sleewell.mvp.menu.routine

import com.sleewell.sleewell.database.routine.entities.Routine
import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

interface RoutineContract {

    interface Model {
        fun createNewItemRoutine()

        fun updateListViewRoutine()

        fun getAdapter() : RoutineListAdapter

        fun removeNewItemRoutine(routine: Routine)

        fun openRoutineParameter(nbr: Int)

        fun updateItemRoutine(routine: Routine, nbr: Int)
    }

    interface Presenter : BasePresenter {
        /**
         * this method is not use here
         *
         * @author gabin warnier de wailly
         */
        fun onViewCreated()

        fun createNewItemRoutine()

        fun removeItemRoutine(nbr: Int)

        fun updateAdapter()

        fun openRoutineDialog(nbr: Int)
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

        fun displayRoutineList(routineAdapter: RoutineListAdapter)
    }
}