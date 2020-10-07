package com.sleewell.sleewell.modules.settings

import android.content.Context
import androidx.preference.PreferenceManager
import com.sleewell.sleewell.R

class SettingsManager(private val ctx: Context) : ISettingsManager {
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx)

    /**
     * Get the wifi state setting
     *
     * @return True - Wifi should be on | False - Wifi should be false
     * @author Hugo Berthomé
     */
    override fun getWifi(): Boolean {
        val defaultValue = ctx.resources.getBoolean(R.bool.setting_wifi_default_value)
        return sharedPref.getBoolean(ctx.resources.getString(R.string.setting_wifi_key), defaultValue)
    }

    /**
     * Set the wifi state setting
     *
     * @param state True - Wifi should be on | False - Wifi should be false
     * @author Hugo Berthomé
     */
    override fun setWifi(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(ctx.resources.getString(R.string.setting_wifi_key), state)
            commit()
        }
    }

    /**
     * Get the bluetooth state setting
     *
     * @return True - bluetooth should be on | False - bluetooth should be false
     * @author Hugo Berthomé
     */
    override fun getBluetooth(): Boolean {
        val defaultValue = ctx.resources.getBoolean(R.bool.setting_bluetooth_default_value)
        return sharedPref.getBoolean(ctx.resources.getString(R.string.setting_bluetooth_key), defaultValue)
    }

    /**
     * Set the bluetooth state setting
     *
     * @param state True - bluetooth should be on | False - bluetooth should be false
     * @author Hugo Berthomé
     */
    override fun setBluetooth(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(ctx.resources.getString(R.string.setting_bluetooth_key), state)
            commit()
        }
    }

    /**
     * Get the cellular state setting
     *
     * @return True - cellular should be on | False - cellular should be false
     * @author Hugo Berthomé
     */
    override fun getCellular(): Boolean {
        val defaultValue = ctx.resources.getBoolean(R.bool.setting_cellular_default_value)
        return sharedPref.getBoolean(ctx.resources.getString(R.string.setting_cellular_key), defaultValue)
    }

    /**
     * Set the cellular state setting
     *
     * @param state True - cellular should be on | False - cellular should be false
     * @author Hugo Berthomé
     */
    override fun setCellular(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(ctx.resources.getString(R.string.setting_cellular_key), state)
            commit()
        }
    }

    /**
     * Get the do not disturb state setting
     *
     * @return True - do not disturb should be on | False - do not disturb should be false
     * @author Hugo Berthomé
     */
    override fun getDnd(): Boolean {
        val defaultValue = ctx.resources.getBoolean(R.bool.setting_dnd_default_value)
        return sharedPref.getBoolean(ctx.resources.getString(R.string.setting_dnd_key), defaultValue)
    }

    /**
     * Set the do not disturb state setting
     *
     * @param state True - do not disturb should be on | False - do not disturb should be false
     * @author Hugo Berthomé
     */
    override fun setDnd(state: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(ctx.resources.getString(R.string.setting_dnd_key), state)
            commit()
        }
    }

}