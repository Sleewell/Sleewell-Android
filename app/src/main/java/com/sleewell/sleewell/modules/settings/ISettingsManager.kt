package com.sleewell.sleewell.modules.settings

/**
 * Interface to manage internal application settings
 * @author Hugo Berthomé
 */
interface ISettingsManager {

    /**
     * Get the wifi state setting
     *
     * @return True - Wifi should be on | False - Wifi should be false
     * @author Hugo Berthomé
     */
    fun getWifi() : Boolean

    /**
     * Set the wifi state setting
     *
     * @param state True - Wifi should be on | False - Wifi should be false
     * @author Hugo Berthomé
     */
    fun setWifi(state : Boolean)

    /**
     * Get the bluetooth state setting
     *
     * @return True - bluetooth should be on | False - bluetooth should be false
     * @author Hugo Berthomé
     */
    fun getBluetooth() : Boolean

    /**
     * Set the bluetooth state setting
     *
     * @param state True - bluetooth should be on | False - bluetooth should be false
     * @author Hugo Berthomé
     */
    fun setBluetooth(state : Boolean)

    /**
     * Get the initial state of the bluetooth before starting the protocol
     *
     * @return True - bluetooth was on
     * @author Hugo Berthomé
     */
    fun getInitialStateBluetooth() : Boolean

    /**
     * Set the initial state of the bluetooth before starting the protocol
     *
     * @param state True - bluetooth was on | False - bluetooth was false
     * @author Hugo Berthomé
     */
    fun setInitialStateBluetooth(state : Boolean)

    /**
     * Get the cellular state setting
     *
     * @return True - cellular should be on | False - cellular should be false
     * @author Hugo Berthomé
     */
    fun getCellular() : Boolean

    /**
     * Set the cellular state setting
     *
     * @param state True - cellular should be on | False - cellular should be false
     * @author Hugo Berthomé
     */
    fun setCellular(state : Boolean)

    /**
     * Get the do not disturb state setting
     *
     * @return True - do not disturb should be on | False - do not disturb should be false
     * @author Hugo Berthomé
     */
    fun getDnd() : Boolean

    /**
     * Set the do not disturb state setting
     *
     * @param state True - do not disturb should be on | False - do not disturb should be false
     * @author Hugo Berthomé
     */
    fun setDnd(state : Boolean)

    /**
     * Get the on boarding state setting
     *
     * @return True - on boarding should be on | False - do not disturb should be false
     * @author Titouan Fiancette
     */
    fun getTutorial() : Boolean

    /**
     * Set the do not disturb state setting
     *
     * @param state True - on boarding should be on | False - do not disturb should be false
     * @author Titouan Fiancette
     */
    fun setTutorial(state : Boolean)
}