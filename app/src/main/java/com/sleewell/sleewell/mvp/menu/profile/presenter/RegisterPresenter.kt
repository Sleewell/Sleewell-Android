package com.sleewell.sleewell.mvp.menu.profile.presenter

import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ResultRegisterSleewell
import com.sleewell.sleewell.mvp.menu.profile.contract.RegisterContract
import com.sleewell.sleewell.mvp.menu.profile.model.RegisterModel
import com.sleewell.sleewell.mvp.menu.profile.view.RegisterFragment

class RegisterPresenter(view: RegisterFragment, context: Context) : RegisterContract.Presenter, RegisterContract.Model.OnFinishedListener  {
    private var view: RegisterFragment? = view
    private var model: RegisterModel = RegisterModel(context)
    private var context = context

    override fun onViewCreated() {
    }

    override fun register(loginId: String, password: String, email: String, firstName: String, lastName: String) {
        model.registerToSleewellApi(this, loginId, password, email, firstName, lastName)
    }

    override fun onDestroy() {
        view = null
    }

    override fun onFinished(registerResult: ResultRegisterSleewell) {
        registerResult.AccessToken?.let { view?.setAccessToken(it) }
    }

    override fun onFailure(t: Throwable) {
        view?.hideLoading()
        if (t.message != null)
            view?.displayToast("An error occurred")
    }
}