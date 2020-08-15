package com.sleewell.sleewell.networkManagement

/**
 * Operates all the devise network settings operation as enable wifi for example
 * @author Hugo Berthomé
 */
interface INetworkManagement {
    /**
     * initPermissions
     *
     * Initialise the permission to access bluetooth and wifi config
     * @author Hugo Berthomé
     */
    fun initPermissions()

    /**
     * Enable / disable bluetooth on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    fun enableBluetooth(value: Boolean)

    /**
     * Enable / disable wifi on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    fun enableWifi(value: Boolean)

    /**
     * Enable / disable Do not Disturb on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    fun enableZenMode(value: Boolean)

    /**
     * Enable / disable night mode
     *  - disable wifi
     *  - disable bluetooth
     *  - enable zen mode
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    fun switchToSleepMode(value: Boolean)
}