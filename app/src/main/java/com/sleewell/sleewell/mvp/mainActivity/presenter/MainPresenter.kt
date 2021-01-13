package com.sleewell.sleewell.mvp.mainActivity.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.mainActivity.MainContract
import com.sleewell.sleewell.modules.network.INetworkManager
import com.sleewell.sleewell.modules.network.NetworkManager

/**
 * Presenter for the main activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the MainContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class MainPresenter(view: MainContract.View, ctx: Context) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private val connection: INetworkManager = NetworkManager(ctx)

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
        connection.initPermissions()
    }
}