package com.sleewell.sleewell.modules.lockScreen

import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Implementation of the ILockScreenManagement
 *
 * @property ctx context of the activity / view
 * @author Titouan FIANCETTE
 */
class LockScreenManager(private val ctx: AppCompatActivity) : ILockScreenManager {

    /**
     * enableShowWhenLock
     *
     * Enables the show when lock option for an activity
     * @author Titouan FIANCETTE
     */
    override fun enableShowWhenLock() {
        ctx.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            ctx.setShowWhenLocked(true)
        } else {
            ctx.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }
    }

    /**
     * initPermissions
     *
     * Enables the show when lock option for an activity
     * @author Titouan FIANCETTE
     */
    override fun disableShowWhenLock() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            ctx.setShowWhenLocked(false)
        } else {
            ctx.window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }
    }

    /**
     * enableKeepScreenOn
     *
     * Enables keep screen on option for an activity
     * @author Titouan FIANCETTE
     */
    override fun enableKeepScreenOn() {
        ctx.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * disableKeepScreenOn
     *
     * Disables keep screen on option option for an activity
     * @author Titouan FIANCETTE
     */
    override fun disableKeepScreenOn() {
        ctx.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}