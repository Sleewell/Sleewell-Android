package com.sleewell.sleewell.mvp.menu.connection.presenter

import android.content.Context
import com.sleewell.sleewell.Spotify.Model.SpotifyModel
import com.sleewell.sleewell.Spotify.View.SpotifyFragment
import com.sleewell.sleewell.mvp.menu.connection.ConnectionContract
import com.sleewell.sleewell.mvp.menu.connection.model.ConnectionModel
import com.sleewell.sleewell.mvp.menu.connection.view.ConnectionFragment

class ConnectionPresenter(view: ConnectionFragment, context: Context) : ConnectionContract.Presenter {
    private var view: ConnectionFragment? = view
    private var model: ConnectionModel = ConnectionModel(context)
    private var context = context


    override fun onViewCreated() {
    }

    override fun onDestroy() {
    }

}