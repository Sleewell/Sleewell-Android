package com.sleewell.sleewell.mvp.mainActivity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener
import com.sleewell.sleewell.mvp.mainActivity.MainContract
import com.sleewell.sleewell.mvp.mainActivity.presenter.MainPresenter
import android.content.Intent
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity

class MainActivity : AppCompatActivity(), MainContract.View {
    private var userInteractionListener: UserInteractionListener? = null
    private lateinit var presenter : MainContract.Presenter

  companion object {
        var getAccessToken: Boolean = false
        lateinit var accessToken: String

    }
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)
        setPresenter(MainPresenter(this, this))
        presenter.onViewCreated()
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
        this.userInteractionListener = userInteractionListener!!
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
}