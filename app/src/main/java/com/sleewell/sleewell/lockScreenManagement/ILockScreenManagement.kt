package com.sleewell.sleewell.lockScreenManagement

/**
 * Operates to manage the lock screen
 * @author Titouan FIANCETTE
 */
interface ILockScreenManagement {
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