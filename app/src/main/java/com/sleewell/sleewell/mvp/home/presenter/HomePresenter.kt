package com.sleewell.sleewell.mvp.home.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.home.HomeContract
import com.sleewell.sleewell.mvp.home.model.HomeModel
import com.sleewell.sleewell.mvp.home.model.NfcState

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
    private var model: HomeContract.Model = HomeModel(context)

    override fun onViewCreated() {
        view?.displayNfcButton(model.nfcState() != NfcState.Enable)
    }

    override fun onDestroy() {
        view = null
    }
}