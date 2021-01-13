package com.sleewell.sleewell.mvp.menu.presenter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.modules.lockScreen.ILockScreenManager
import com.sleewell.sleewell.modules.lockScreen.LockScreenManager
import com.sleewell.sleewell.mvp.menu.MenuContract

/**
 * Presenter for the Menu fragment, it will link the MenuView and the MenuModel
 *
 * @constructor Creates a presenter based on the Home Contract
 * @param view View that inherits the View from the home contract
 * @param context Context of the activity / view
 * @author Hugo Berthomé
 */
class MenuPresenter(private var view: MenuContract.View, private val ctx: AppCompatActivity) : MenuContract.Presenter {
    private val lockScreen: ILockScreenManager = LockScreenManager(ctx)

    override fun onViewCreated() {
        lockScreen.disableShowWhenLock()
    }

    override fun onDestroy() {
    }
}