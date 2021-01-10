package com.sleewell.sleewell.mvp.mainActivity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener


class MainActivity : AppCompatActivity() {
    private var userInteractionListener: UserInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)
    }

    fun setUserInteractionListener(userInteractionListener: UserInteractionListener?) {
        this.userInteractionListener = userInteractionListener!!
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (userInteractionListener != null) userInteractionListener?.onUserInteraction()
    }
}