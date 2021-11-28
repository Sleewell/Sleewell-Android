package com.sleewell.sleewell.mvp.protocol.view

import android.animation.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListener
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter
import kotlinx.android.synthetic.main.activity_protocol_container.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProtocolContainer : AppCompatActivity(), ProtocolMenuContract.View {

    private lateinit var presenter: ProtocolMenuContract.Presenter

    private var shortAnimationDuration: Int = 0
    private var inactivityDuration: Long = 4 // in seconds
    private var displayHaloRunnable = Runnable { displayHalo() }
    private var handler = Handler(Looper.getMainLooper())
    private var isHaloDisplayed: Boolean = false
    private lateinit var haloAnimation: ObjectAnimator
    private var brightnessValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_container)

        setPresenter(ProtocolPresenter(this, this))
        initActivityWidgets()
        AnimationUtils.loadAnimation(this, R.anim.slide_in_up).also { animation ->
            protocolLayout.startAnimation(animation)
        }

        if (Settings.System.canWrite(this))
            brightnessValue = Settings.System.getInt(contentResolver, SCREEN_BRIGHTNESS)

    }

    private fun initActivityWidgets() {
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d LLL"))
        }


        equalizer.setOnClickListener {
            presenter.pauseMusic()
        }
        equalizer.stopBars()

        protocolLayout.setOnTouchListener(object : OnSwipeListener(applicationContext) {
            override fun onSwipeTop() {
                quitActivity()
            }

            override fun onSwipeBottom() {}
            override fun onSwipeLeft() {}
            override fun onSwipeRight() {}
        })

        haloAnimation = ObjectAnimator.ofPropertyValuesHolder(
            halo,
            PropertyValuesHolder.ofFloat("scaleX", 0.01f),
            PropertyValuesHolder.ofFloat("scaleY", 0.01f)
        )
        haloAnimation.duration = 5000
        haloAnimation.repeatMode = ValueAnimator.REVERSE
        haloAnimation.repeatCount = ValueAnimator.INFINITE
        haloAnimation.start()
    }

    /**
     * Function to call for triggering the animation and leaving the protocol
     * @author Hugo Berthomé
     */
    fun quitActivity() {
        AnimationUtils.loadAnimation(this, R.anim.slide_out_up).also { animation ->
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    finish()
                }
            })
            protocolLayout.startAnimation(animation)
        }
    }

    override fun undisplayEquilizer() {
        equalizer.visibility = View.GONE
    }

    override fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.fitsSystemWindows = false
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    override fun showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.decorView.systemUiVisibility = 0
        }
    }

    /**
     * Hide the halo if its on or reset inactivity timer if its off
     * @author Titouan FIANCETTE
     */
    override fun haloDisplayLooper() {
        if (isHaloDisplayed) {
            hideHalo()
            handler.postDelayed(displayHaloRunnable, inactivityDuration * 1000)
        } else if (presenter.isHaloOn()) {
            handler.removeCallbacks(displayHaloRunnable)
            handler.postDelayed(displayHaloRunnable, inactivityDuration * 1000)
        }
    }

    override fun stopAnimation() {
        haloAnimation.end()
        halo.visibility = View.GONE
    }

    private fun displayHalo() {
        isHaloDisplayed = true
        window.statusBarColor = resources.getColor(android.R.color.black, resources.newTheme())

        haloBackground.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f).setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }

        halo.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f).setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
        if (Settings.System.canWrite(this))
            Settings.System.putInt(contentResolver, SCREEN_BRIGHTNESS, 10000)
    }

    private fun hideHalo() {
        isHaloDisplayed = false
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark, resources.newTheme())

        haloBackground.animate().alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    haloBackground.visibility = View.GONE
                }
            })

        halo.animate().alpha(0f)
            .setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    halo.visibility = View.GONE
                }
            })
        if (Settings.System.canWrite(this))
            Settings.System.putInt(contentResolver, SCREEN_BRIGHTNESS, brightnessValue)
    }

    override fun setHaloColor(color: Int) {
        val unwrappedDrawable = AppCompatResources.getDrawable(applicationContext, R.drawable.halo_test)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)
        halo.background = wrappedDrawable
    }

    override fun isMusicPlaying(): Boolean {
        return equalizer.isAnimating
    }

    override fun animateEqualizer(state: Boolean) {
        if (state) { equalizer.animateBars() }
        else { equalizer.stopBars() }
    }

    override fun setPresenter(presenter: ProtocolMenuContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        haloDisplayLooper()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(displayHaloRunnable)
        if (Settings.System.canWrite(this))
            Settings.System.putInt(contentResolver, SCREEN_BRIGHTNESS, brightnessValue)
        haloAnimation.end()
        presenter.onDestroy()
    }
}