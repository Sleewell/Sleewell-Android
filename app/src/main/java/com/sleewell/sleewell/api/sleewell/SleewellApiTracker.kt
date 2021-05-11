package com.sleewell.sleewell.api.sleewell

import android.content.Context
import android.content.SharedPreferences
import com.sleewell.sleewell.R

/**
 * Retrieve the token or set token for Sleewell api
 *
 * @author Hugo Berthomé
 */
class SleewellApiTracker {

    companion object {
        /**
         * Set token in shared preference
         *
         * @param context
         * @param token
         * @author Hugo Berthomé
         */
        fun setToken(context: Context, token: String) {
            val sharedPrefs = getPreferences(context)
            sharedPrefs.edit().let {
                it.putString(context.getString(R.string.user_token_key), token)
                it.apply()
            }
        }

        /**
         * Disconnect application from API
         *
         * @param context
         * @author Hugo Berthomé
         */
        fun disconnect(context: Context) {
            val sharedPrefs = getPreferences(context)
            sharedPrefs.edit().let {
                it.putString(context.getString(R.string.user_token_key), "")
                it.apply()
            }
        }

        /**
         * Return if the application has been connected to the API
         *
         * @param context
         * @return true is connected, false otherwise
         * @author Hugo Berthomé
         */
        fun isConnected(context: Context) : Boolean {
            val sharedPrefs = getPreferences(context)
            val value = sharedPrefs.getString(context.getString(R.string.user_token_key), "")
            return !value.isNullOrEmpty()
        }

        /**
         * Return the current token
         *
         * @param context
         * @return token, empty if no token
         * @author Hugo Berthomé
         */
        fun getToken(context: Context): String {
            val sharedPrefs = getPreferences(context)
            return sharedPrefs.getString(context.getString(R.string.user_token_key), "")
                ?: return ""
        }

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.getString(R.string.sharedPrefFile), 0)
        }
    }
}