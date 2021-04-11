package com.sleewell.sleewell.mvp.menu.profile.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.profile.ProfileContract
import com.sleewell.sleewell.mvp.menu.profile.model.ProfileModel

/**
 * Presenter for the Profile fragment, it will link the HomeView and the HomeModel
 *
 * @constructor Creates a presenter based on the Contract
 * @param view View that inherits the View from the Contract
 * @param context Context of the activity / view
 * @author Hugo Berthomé
 */
class ProfilePresenter(view: ProfileContract.View, context: Context) : ProfileContract.Presenter {

    private var view: ProfileContract.View? = view
    private var model: ProfileContract.Model = ProfileModel(context)

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        view = null;
    }
}