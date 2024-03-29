package com.sleewell.sleewell.mvp.menu.home.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.home.HomeContract

/**
 * Presenter for the Home fragment, it will link the HomeView and the HomeModel
 *
 * @constructor Creates a presenter based on the Home Contract
 * @param view View that inherits the View from the home contract
 * @param context Context of the activity / view
 * @author Hugo Berthomé
 */
class HomePresenter(view: HomeContract.View, context: Context) : HomeContract.Presenter {

    private var view: HomeContract.View? = view

    /**
     * Function to call at the creation of the view
     *
     * @author Hugo Berthomé
     */
    override fun onViewCreated() {
    }

    /**
     * onDestroy is called at each time e presenter will be destroyed
     * @author Hugo Berthomé
     */
    override fun onDestroy() {
        view = null
    }
}