package com.sleewell.sleewell.mvp.menu.profile.presenter

import android.content.Context
import com.sleewell.sleewell.api.sleewell.model.ResultLoginSleewell
import com.sleewell.sleewell.mvp.menu.profile.contract.LoginContract
import com.sleewell.sleewell.mvp.menu.profile.model.LoginModel
import com.sleewell.sleewell.mvp.menu.profile.view.LoginFragment

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

    override fun loginGoogle(token: String) {
        model.loginToGoogle(this, token)
    }

    override fun onFinished(loginResult: ResultLoginSleewell) {
        loginResult.accessToken?.let { view?.setAccessToken(it) }
    }


    override fun onFailure(t: Throwable) {
        view?.hideLoading()
        if (t.message != null)
            view?.displayToast("Wrong credentials")
        else
            view?.displayToast("An error occurred")
    }
}