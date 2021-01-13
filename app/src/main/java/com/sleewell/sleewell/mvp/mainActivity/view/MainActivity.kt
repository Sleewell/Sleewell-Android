package com.sleewell.sleewell.mvp.mainActivity.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sleewell.sleewell.R
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.authentication.LoginActivity

class MainActivity : AppCompatActivity() {

    companion object {
        var getAccessToken: Boolean = false
        lateinit var accessToken: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)
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