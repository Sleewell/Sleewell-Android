package com.sleewell.sleewell.modules.permissions

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * Class that ask for all permissions required for the application
 *
 * @property context AppCompatActivity activity where the popup will display
 */
class PermissionManager(private val context: AppCompatActivity) {
    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    /**
     * Ask for all the permissions required on the phone
     *
     */
    fun askAllPermission() {
        askRecordAudioPermission()
        askNotificationPermission()
    }

    /**
     * Ask about audio permission
     *
     */
    fun askRecordAudioPermission() {
        if (!permissionGrantedAudioRecord())
            context.requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    /**
     * Check if the permission for audio record has been granted
     *
     * @return Boolean
     */
    fun permissionGrantedAudioRecord(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Ask for do not disturb permission
     *
     */
    fun askNotificationPermission() {
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(context, DndAccessTutorialActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun isNotificationPolicyAccessGranted(): Boolean {
        return notificationManager.isNotificationPolicyAccessGranted
    }

    /**
     * Check if the permission for do not disturb has been granted
     *
     * @return Boolean
     */
    fun permissionGrantedNotification() : Boolean {
        return notificationManager.isNotificationPolicyAccessGranted
    }
}