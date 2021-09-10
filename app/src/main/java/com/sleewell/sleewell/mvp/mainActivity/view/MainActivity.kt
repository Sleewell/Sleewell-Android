package com.sleewell.sleewell.mvp.mainActivity.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener
import com.sleewell.sleewell.mvp.mainActivity.MainContract
import com.sleewell.sleewell.mvp.mainActivity.presenter.MainPresenter
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.modules.audio.upload.AudioAnalyseUpload
import com.sleewell.sleewell.modules.permissions.PermissionManager
import com.sleewell.sleewell.mvp.menu.profile.view.GivenImagesDialog
import com.sleewell.sleewell.mvp.menu.profile.view.PickImageDialog
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity

class MainActivity : AppCompatActivity(), MainContract.View,
    PickImageDialog.DialogEventListener, GivenImagesDialog.DialogEventListener {
    private var userInteractionListener: UserInteractionListener? = null
    private var pickDialogEventListener: PickImageDialog.DialogEventListener? = null
    private var givenDialogEventListener: GivenImagesDialog.DialogEventListener? = null
    private lateinit var presenter: MainContract.Presenter
    private lateinit var statsUpload : AudioAnalyseUpload

    companion object {
        var getAccessTokenSpotify: Boolean = false
        lateinit var accessTokenSpotify: String

        var accessTokenSleewell: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.new_activity_main)
        statsUpload = AudioAnalyseUpload(applicationContext)
        setPresenter(MainPresenter(this))
        presenter.onViewCreated()
        accessTokenSleewell = getAccessToken()
    }

    fun getAccessToken() : String {
        return SleewellApiTracker.getToken(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        askAuthorisation();
    }

    override fun onResume() {
        super.onResume()
        statsUpload.updateUpload()
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    /**
     * Sets the listening Fragment with the callback to be executed onUserInteraction event.
     * This function needs to be callback by the Fragment itself
     *
     * @param userInteractionListener Fragment implementing UserInteractionListener
     * @author Titouan FIANCETTE
     */
    fun setUserInteractionListener(userInteractionListener: UserInteractionListener?) {
        if (userInteractionListener == null) {
            this.userInteractionListener = null
        } else {
            this.userInteractionListener = userInteractionListener
        }
    }

    /**
     * Overrides the onUserInteraction callback to pass the event to a Fragment
     * @author Titouan FIANCETTE
     */
    override fun onUserInteraction() {
        super.onUserInteraction()
        if (userInteractionListener != null) userInteractionListener?.onUserInteraction()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == LoginActivity.REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    accessTokenSpotify = response.accessToken
                    getAccessTokenSpotify = true
                }
                AuthenticationResponse.Type.ERROR -> {
                    getAccessTokenSpotify = false
                }
                else -> {
                }
            }
        }
    }

    /**
     * We need to create a notification channel for using for all the application
     * Here create for the analyse
     * MANDATORY DO NOT DELETE
     * @author Hugo Berthomé
     */
    private fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val test = resources.getString(R.string.notification_analyse_channel_description)
            val channel = NotificationChannel(
                resources.getString(R.string.notification_analyse_channel_id),
                resources.getString(R.string.notification_analyse_channel_name),
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = resources.getString(R.string.notification_analyse_channel_description)
                enableLights(true)
                lightColor = Color.argb(255, 207, 123, 45)
                enableVibration(false)
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Ask for all the permissions of the application
     * MANDATORY DO NOT DELETE
     */
    private fun askAuthorisation() {
        val permissionManager = PermissionManager(this)
        permissionManager.askAllPermission()
    }

    fun setPickDialogEventListener(listener: PickImageDialog.DialogEventListener?) {
        this.pickDialogEventListener = listener
    }

    override fun onDialogTakePictureClick(dialog: DialogFragment) {
        pickDialogEventListener?.onDialogTakePictureClick(dialog)
    }

    override fun onDialogPickPictureClick(dialog: DialogFragment) {
        pickDialogEventListener?.onDialogPickPictureClick(dialog)
    }

    override fun onDialogGivenPictureClick(dialog: DialogFragment) {
        pickDialogEventListener?.onDialogGivenPictureClick(dialog)
    }

    fun setGivenDialogEventListener(listener: GivenImagesDialog.DialogEventListener?) {
        this.givenDialogEventListener = listener
    }

    override fun onDialogPictureClick(picture: ImageView) {
        givenDialogEventListener?.onDialogPictureClick(picture)
    }
}