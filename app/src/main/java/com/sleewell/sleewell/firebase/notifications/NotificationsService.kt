package com.sleewell.sleewell.firebase.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sleewell.sleewell.R
import com.sleewell.sleewell.splashScreen.SplashScreenActivity

/**
 * Notification Receiver from Firebase display a notification on the phone
 *
 * @author Hugo Berthomé
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationsService : FirebaseMessagingService() {

    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "FIREBASE_SLEEWELL"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            val notification = remoteMessage.notification!!
            sendVisualNotification(notification);
        }
    }

    /**
     * Display the notification received
     *
     * @param notification
     * @author Hugo Berthomé
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendVisualNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, SplashScreenActivity::class.java)
        val pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "General"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pending)
            .setSmallIcon(R.drawable.ic_sleewell)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Firebase Messages"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build())
    }
}