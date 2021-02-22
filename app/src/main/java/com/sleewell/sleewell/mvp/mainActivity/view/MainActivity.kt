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
import android.util.Log
import androidx.room.Room
import com.sleewell.sleewell.database.analyse.night.Analyse
import com.sleewell.sleewell.database.analyse.night.Night
import com.sleewell.sleewell.database.analyse.night.NightDatabase
import com.sleewell.sleewell.modules.permissions.PermissionManager
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainContract.View {
    private var userInteractionListener: UserInteractionListener? = null
    private lateinit var presenter: MainContract.Presenter

    companion object {
        var getAccessToken: Boolean = false
        lateinit var accessToken: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.new_activity_main)
        setPresenter(MainPresenter(this))

        /*val db = NightDatabase.getDatabase(applicationContext)
        val nightDao = db.nightDao()
        val analyseDao = db.analyseDao()

        CoroutineScope(Job() + Dispatchers.IO).launch {
            val night = Night(start = 10, end = 20)
            val nightId = nightDao.insertNight(night)

            val analyses = listOf(
                Analyse(nightId = nightId, db = 1.0),
                Analyse(nightId = nightId, db = 2.0),
                Analyse(nightId = nightId, db = 3.0),
                Analyse(nightId = nightId, db = 4.0),
                Analyse(nightId = nightId, db = 5.0),
                Analyse(nightId = nightId, db = 6.0),
                Analyse(nightId = nightId, db = 7.0),
                Analyse(nightId = nightId, db = 8.0),
            )

            analyses.forEach { it -> analyseDao.insertAnalyse(it) }

            val resNightAnalyse = nightDao.getNightAnalyse(nightId)

            val res = nightDao.getAll()
            *//*res.forEach { nightIt -> nightDao.deleteNight(nightIt) }*//*
            val resAnalyse = analyseDao.getAll()
            *//*resAnalyse.forEach { it -> analyseDao.deleteAnalyse(it) }*//*
            *//*db.close()*//*
        }*/

        presenter.onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        askAuthorisation();
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
                    accessToken = response.accessToken
                    getAccessToken = true
                }
                AuthenticationResponse.Type.ERROR -> {
                    getAccessToken = false
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
}