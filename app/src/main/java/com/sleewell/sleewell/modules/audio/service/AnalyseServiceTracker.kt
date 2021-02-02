package com.sleewell.sleewell.modules.audio.service

import android.content.Context
import android.content.SharedPreferences

/**
 * Get and save the service analyse state in the preference of the phone
 *
 * @author Hugo Berthomé
 */
class AnalyseServiceTracker {
    enum class ServiceState {
        STARTED,
        STOPPED,
    }

    companion object {
        private val name = "ANALYSE_SERVICE"
        private val key = "ANALYSE_STATE"

        fun setServiceState(context: Context, state: ServiceState) {
            val sharedPrefs = getPreferences(context)
            sharedPrefs.edit().let {
                it.putString(key, state.name)
                it.apply()
            }
        }

        fun getServiceState(context: Context): ServiceState {
            val sharedPrefs = getPreferences(context)
            val value = sharedPrefs.getString(key, ServiceState.STOPPED.name) ?: return ServiceState.STOPPED
            return ServiceState.valueOf(value)
        }

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(name, 0)
        }
    }
}