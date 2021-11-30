package com.sleewell.sleewell.mvp.mainActivity.presenter

import com.sleewell.sleewell.mvp.mainActivity.MainContract

/**
 * Presenter for the main activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the MainContract.View
 * @author Hugo Berthomé
 */
class MainPresenter(view: MainContract.View) : MainContract.Presenter {

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
    }

    /**
     * Function to call at the creation of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewCreated() {
    }
}