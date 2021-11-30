package com.sleewell.sleewell.mvp.mainActivity.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sleewell.sleewell.R
import com.sleewell.sleewell.api.sleewell.SleewellApiTracker
import com.sleewell.sleewell.database.tokenData
import com.sleewell.sleewell.modules.audio.upload.AudioAnalyseUpload
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener
import com.sleewell.sleewell.modules.permissions.PermissionManager
import com.sleewell.sleewell.modules.settings.ISettingsManager
import com.sleewell.sleewell.modules.settings.SettingsManager
import com.sleewell.sleewell.mvp.help.OnBoardingActivity
import com.sleewell.sleewell.mvp.mainActivity.MainContract
import com.sleewell.sleewell.mvp.mainActivity.presenter.MainPresenter
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.DeleteDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.GivenImagesDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.PickImageDialog
import com.sleewell.sleewell.mvp.menu.profile.view.dialogs.ProfileBottomSheet
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import kotlinx.android.synthetic.main.new_activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), MainContract.View,
    PickImageDialog.DialogEventListener, GivenImagesDialog.DialogEventListener,
    DeleteDialog.DialogEventListener, ProfileBottomSheet.DialogEventListener {
    private var userInteractionListener: UserInteractionListener? = null

    private var pickDialogEventListener: PickImageDialog.DialogEventListener? = null
    private var givenDialogEventListener: GivenImagesDialog.DialogEventListener? = null
    private var deleteDialogEventListener: DeleteDialog.DialogEventListener? = null
    private var bottomSheetListener: ProfileBottomSheet.DialogEventListener? = null

    private lateinit var settings : ISettingsManager

    private lateinit var presenter: MainContract.Presenter
    private lateinit var statsUpload: AudioAnalyseUpload

    companion object {
        var getAccessTokenSpotify: Boolean = false
        lateinit var accessTokenSpotify: String

        var accessTokenSleewell: String = ""

        var getAccessGoogleAccount: Boolean = false
        lateinit var sendTokenToSleewell: (token : String) -> Unit

        var allToken: tokenData? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.new_activity_main)
        statsUpload = AudioAnalyseUpload(this)
        setPresenter(MainPresenter(this))
        presenter.onViewCreated()
        accessTokenSleewell = getAccessToken()
        settings = SettingsManager(this)

        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
        readToken()
    }

    fun getAccessToken(): String {
        return SleewellApiTracker.getToken(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        askAuthorisation()
        showOnBoardingTutorial()
        stars_white.onStart()
    }

    override fun onResume() {
        super.onResume()
        statsUpload.updateUpload()
    }

    override fun onStop() {
        stars_white.onStop()
        super.onStop()
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
        if (requestCode == 9001) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(intent).getResult(ApiException::class.java)
                getAccessGoogleAccount = account?.account.toString().isNotEmpty()
                if (getAccessGoogleAccount) {
                    sendTokenToSleewell(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.e("ERROR", e.status.toString())
                getAccessGoogleAccount = false
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

    private fun showOnBoardingTutorial() {
        val permissionManager = PermissionManager(this)
        if (permissionManager.isNotificationPolicyAccessGranted() && settings.getTutorial()) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        }
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

    fun setDeleteDialogEventListener(listener: DeleteDialog.DialogEventListener?) {
        this.deleteDialogEventListener = listener
    }

    override fun onContinue() {
        deleteDialogEventListener?.onContinue()
    }

    override fun onItem1Click() {
        bottomSheetListener?.onItem1Click()
    }

    override fun onItem2Click() {
        bottomSheetListener?.onItem2Click()
    }

    fun setBottomSheetEventListener(listener: ProfileBottomSheet.DialogEventListener?) {
        this.bottomSheetListener = listener
    }

    fun readToken() {
        val jsonFileString = try {
            applicationContext.assets.open("token.json").bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            null
        }

        if (jsonFileString.isNullOrEmpty())
            return

        val gson = Gson()
        val demandeType = object : TypeToken<tokenData>() {}.type

        allToken = gson.fromJson(jsonFileString, demandeType)
    }
}