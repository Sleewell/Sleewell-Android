package com.sleewell.sleewell.modules.lockScreen

/**
 * Operates to manage the lock screen
 * @author Titouan FIANCETTE
 */
interface ILockScreenManager {
    /**
     * enableShowWhenLock
     *
     * Enables the show when lock option for an activity
     * @author Titouan FIANCETTE
     */
    fun enableShowWhenLock()

    /**
     * initPermissions
     *
     * Disables the show when lock option for an activity
     * @author Titouan FIANCETTE
     */
    fun disableShowWhenLock()

    /**
     * enableKeepScreenOn
     *
     * Enables keep screen on option for an activity
     * @author Titouan FIANCETTE
     */
    fun enableKeepScreenOn()

    /**
     * disableKeepScreenOn
     *
     * Disables keep screen on option option for an activity
     * @author Titouan FIANCETTE
     */
    fun disableKeepScreenOn()
}