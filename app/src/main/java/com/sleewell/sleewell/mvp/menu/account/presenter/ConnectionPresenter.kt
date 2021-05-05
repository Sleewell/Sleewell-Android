package com.sleewell.sleewell.mvp.menu.account.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.account.ApiResultLoginSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.ConnectionContract
import com.sleewell.sleewell.mvp.menu.account.model.ConnectionModel
import com.sleewell.sleewell.mvp.menu.account.view.ConnectionFragment

class ConnectionPresenter(view: ConnectionFragment, context: Context) : ConnectionContract.Presenter, ConnectionContract.Model.OnFinishedListener {
    private var view: ConnectionFragment? = view
    private var model: ConnectionModel = ConnectionModel(context)

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        view = null
    }

    override fun login(name : String, password: String) {
        model.loginToSleewell(this, name, password)
    }

    override fun onFinished(loginResult: ApiResultLoginSleewell) {
        loginResult.AccessToken?.let { view?.setAccessToken(it) }
    }


    override fun onFailure(t: Throwable) {
        if (t.message != null)
            view?.displayToast("Wrong credentials")
        else
            view?.displayToast("An error occurred")
    }
}