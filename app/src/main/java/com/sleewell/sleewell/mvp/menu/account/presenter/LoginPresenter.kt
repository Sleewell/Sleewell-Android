package com.sleewell.sleewell.mvp.menu.account.presenter

import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ResultLoginSleewell
import com.sleewell.sleewell.mvp.menu.account.contract.LoginContract
import com.sleewell.sleewell.mvp.menu.account.model.LoginModel
import com.sleewell.sleewell.mvp.menu.account.view.LoginFragment

class LoginPresenter(view: LoginFragment, context: Context) : LoginContract.Presenter, LoginContract.Model.OnFinishedListener {
    private var view: LoginFragment? = view
    private var model: LoginModel = LoginModel(context)

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        view = null
    }

    override fun login(name : String, password: String) {
        model.loginToSleewell(this, name, password)
    }

    override fun onFinished(loginResult: ResultLoginSleewell) {
        loginResult.AccessToken?.let { view?.setAccessToken(it) }
    }


    override fun onFailure(t: Throwable) {
        view?.hideLoading()
        if (t.message != null)
            view?.displayToast("Wrong credentials")
        else
            view?.displayToast("An error occurred")
    }
}