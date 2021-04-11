package com.sleewell.sleewell.mvp.menu.profile

import com.sleewell.sleewell.mvp.global.BasePresenter
import com.sleewell.sleewell.mvp.global.BaseView

/**
 * Contract that defines all the functions that will interact with the user
 * @author Titouan Fiancette
 */
interface ProfileContract {
    interface Model {

    }

    interface Presenter : BasePresenter {
        /**
         * Function to call at the creation of the view
         * @author Titouan Fiancette
         */
        fun onViewCreated()
    }

    interface View : BaseView<Presenter> {

    }
}