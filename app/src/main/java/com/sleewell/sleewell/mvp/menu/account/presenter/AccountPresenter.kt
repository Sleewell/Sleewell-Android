package com.sleewell.sleewell.mvp.menu.account.presenter

import android.content.Context
import com.sleewell.sleewell.mvp.menu.account.contract.AccountContract
import com.sleewell.sleewell.mvp.menu.account.view.AccountFragment

class AccountPresenter(view: AccountFragment, context: Context) : AccountContract.Presenter {
    private var view: AccountFragment? = view

    override fun onViewCreated() {
    }

    override fun onDestroy() {
        view = null
    }
}