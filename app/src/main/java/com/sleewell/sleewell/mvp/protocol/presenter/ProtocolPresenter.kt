package com.sleewell.sleewell.mvp.protocol.presenter

import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.networkManagement.INetworkManagement
import com.sleewell.sleewell.networkManagement.NetworkManagement
import org.jetbrains.annotations.Contract

/**
 * Presenter for the protocol activity
 *
 * @constructor Creates an instance of the presenter that link model and view and do all the logic
 * @param view View base on the ProtocolContract.View
 * @param ctx context is from the current activity / view
 * @author Hugo Berthomé
 */
class ProtocolPresenter(view: ProtocolContract.View, ctx: AppCompatActivity) : ProtocolContract.Presenter {

    private var view: ProtocolContract.View? = view
    private val connection: INetworkManagement = NetworkManagement(ctx)

    override fun onDestroy() {
        connection.switchToSleepMode(false)
    }

    override fun onViewCreated() {
        connection.switchToSleepMode(true)
    }
}