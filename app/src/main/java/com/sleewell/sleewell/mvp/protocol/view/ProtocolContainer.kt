package com.sleewell.sleewell.mvp.protocol.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentContainerView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener

class ProtocolContainer : AppCompatActivity() {
    private var userInteractionListener: UserInteractionListener? = null
    lateinit var fragment : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_container)
        fragment = findViewById(R.id.fragment)

        AnimationUtils.loadAnimation(this, R.anim.slide_in_up).also { animation ->
            fragment.startAnimation(animation)
        }
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

    /**
     * Function to call for triggering the animation and leaving the protocol
     *
     * @author Hugo Berthomé
     */
    fun quitActivity() {
        AnimationUtils.loadAnimation(this, R.anim.slide_out_up).also { animation ->
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            fragment.startAnimation(animation)
        }
    }
}