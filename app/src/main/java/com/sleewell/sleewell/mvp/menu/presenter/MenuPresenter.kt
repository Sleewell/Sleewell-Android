package com.sleewell.sleewell.mvp.menu.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.MenuContract

/**
 * Presenter for the Menu fragment, it will link the MenuView and the MenuModel
 *
 * @constructor Creates a presenter based on the Home Contract
 * @param view View that inherits the View from the home contract
 * @param context Context of the activity / view
 * @author Hugo Berthomé
 */
class MenuPresenter(view: MenuContract.View, context: Context) : MenuContract.Presenter {

    private var view: MenuContract.View? = view

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