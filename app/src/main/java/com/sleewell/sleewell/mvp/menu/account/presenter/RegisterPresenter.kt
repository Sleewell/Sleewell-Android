package com.sleewell.sleewell.mvp.menu.account.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.account.ApiResultRegisterSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.RegisterContract
import com.sleewell.sleewell.mvp.menu.account.model.RegisterModel
import com.sleewell.sleewell.mvp.menu.account.view.RegisterFragment

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

    override fun onFinished(registerResult: ApiResultRegisterSleewell) {
        registerResult.AccessToken?.let { view?.setAccessToken(it) }
    }

    override fun onFailure(t: Throwable) {
        view?.hideLoading()
        if (t.message != null)
            view?.displayToast("An error occurred")
    }
}