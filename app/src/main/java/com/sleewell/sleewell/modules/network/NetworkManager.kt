package com.sleewell.sleewell.modules.network

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.sleewell.sleewell.modules.settings.ISettingsManager
import com.sleewell.sleewell.modules.settings.SettingsManager

/**
 * Implementation of the INetworkManager
 *
 * @property ctx context of the activity / view
 * @author Hugo Berthomé
 */
class NetworkManager(private val ctx: Context) : INetworkManager {
    private val bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val notificationManager: NotificationManager =
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val setting : ISettingsManager = SettingsManager(ctx)

    /**
     * initPermissions
     *
     * Initialise the permission to access bluetooth and wifi config
     * @author Hugo Berthomé
     */
    override fun initPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            ctx.startActivity(intent)
        }
    }

    /**
     * Enable / disable bluetooth on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    override fun enableBluetooth(value: Boolean) {
        if (setting.getBluetooth())
            return

        if (value) {
            bAdapter?.enable()
        } else {
            bAdapter?.disable()
        }
    }

    /**
     * Enable / disable wifi on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    override fun enableWifi(value: Boolean) {
        if (setting.getWifi())
            return

        val wifiManager: WifiManager =
            ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        @Suppress("DEPRECATION") // Deprecated from Android 29
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            wifiManager.isWifiEnabled = value
        else {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            ctx.startActivity(panelIntent)
        }
    }

    /**
     * Enable / disable Do not Disturb on the devise
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    override fun enableZenMode(value: Boolean) {
        if (!setting.getDnd())
            return

        // Check if the notification policy access has been granted for the app.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || !notificationManager.isNotificationPolicyAccessGranted)
            return
        if (value) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    /**
     * Enable / disable night mode
     *  - disable wifi
     *  - disable bluetooth
     *  - enable zen mode
     *
     * @param value true - enable, false - disable
     * @author Hugo Berthomé
     */
    override fun switchToSleepMode(value: Boolean) {
        enableBluetooth(!value)
        enableWifi(!value)
        enableZenMode(value)
    }

}