package com.sleewell.sleewell.networkManagement

import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

/**
 * Implementation of the INetworkManagement
 *
 * @property ctx context of the activity / view
 * @author Hugo Berthomé
 */
class NetworkManagement(private val ctx: AppCompatActivity) : INetworkManagement {
    private val bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val notificationManager: NotificationManager =
        ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun initPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            ctx.startActivity(intent)
        }
    }

    override fun enableBluetooth(value: Boolean) {
        if (value) {
            bAdapter?.enable()
        } else {
            bAdapter?.disable()
        }
    }

    override fun enableWifi(value: Boolean) {
        val wifiManager: WifiManager =
            ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        @Suppress("DEPRECATION") // Deprecated from Android 29
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            wifiManager.isWifiEnabled = value
        else {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            ctx.startActivityForResult(panelIntent, 0)
        }
    }

    override fun enableZenMode(value: Boolean) {
        // Check if the notification policy access has been granted for the app.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || !notificationManager.isNotificationPolicyAccessGranted)
            return
        if (value) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    override fun switchToSleepMode(value: Boolean) {
        enableBluetooth(!value)
        enableWifi(!value)
        enableZenMode(value)
    }

}