package com.sleewell.sleewell.mvp.menu.settings.presenter

import com.sleewell.sleewell.mvp.menu.settings.SettingsContract


/**
 * Presenter for the settings activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the SettingsContract.View
 * @author Gabin Warnier de wailly
 */
class SettingsPresenter(view: SettingsContract.View) : SettingsContract.Presenter {

    private var view: SettingsContract.View? = view

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Gabin warnier de wailly
     */
    override fun onDestroy() {
    }

    /**
     * Function to call at the creation of the view
     *
     * @author Gabin warnier de wailly
     */
    override fun onViewCreated() {
    }
}