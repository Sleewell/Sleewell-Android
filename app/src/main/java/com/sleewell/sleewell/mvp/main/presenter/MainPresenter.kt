package com.sleewell.sleewell.mvp.main.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.mvp.main.MainContract
import com.sleewell.sleewell.networkManagement.INetworkManagement
import com.sleewell.sleewell.networkManagement.NetworkManagement

/**
 * Presenter for the main activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the MainContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class MainPresenter(view: MainContract.View, ctx: AppCompatActivity) : MainContract.Presenter {

    private var view: MainContract.View? = view
    private val connection: INetworkManagement = NetworkManagement(ctx)

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