package com.sleewell.sleewell.mvp.protocol.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.Color.blue
import android.graphics.ColorFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListener
import com.sleewell.sleewell.modules.gesturelistener.UserInteractionListener
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolMenuPresenter
import es.claucookie.miniequalizerlibrary.EqualizerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProtocolMenuFragment : Fragment(), ProtocolMenuContract.View, UserInteractionListener {
    //Context
    private lateinit var presenter: ProtocolMenuContract.Presenter
    private lateinit var root: View

    private var shortAnimationDuration: Int = 0
    private var inactivityDuration: Long = 4 // in seconds
    private var handler = Handler()
    private var displayHaloRunnable = Runnable {
        displayHalo()
    }
    private var isHaloDisplayed: Boolean = false

    //widgets

    private lateinit var dateView: TextView
    private lateinit var musicButton: Button
    private lateinit var equalizer: EqualizerView
    private lateinit var container: ConstraintLayout

    private lateinit var halo: ImageView
    private lateinit var haloBackground: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root =  inflater.inflate(R.layout.new_fragment_protocol_menu, container, false)

        (requireActivity() as ProtocolContainer).setUserInteractionListener(this)
        initActivityWidgets()
        setPresenter(ProtocolMenuPresenter(this, this.activity as AppCompatActivity))
        onUserInteraction()

        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initActivityWidgets() {

        dateView = root.findViewById(R.id.date_protocol)
        musicButton = root.findViewById(R.id.musicButton)
        equalizer = root.findViewById<View>(R.id.equalizer_view) as EqualizerView
        container = root.findViewById(R.id.protocol_layout)

        halo =  root.findViewById(R.id.halo)
        haloBackground = root.findViewById(R.id.halo_background)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateView.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d LLL"))
        }

        musicButton.setOnClickListener { presenter.pauseMusic() }

        container.setOnTouchListener(object : OnSwipeListener(root.context) {
            override fun onSwipeTop() {
                presenter.stopAnalyse()
                presenter.disableShowWhenLock()
                showSystemUI()

                if (activity != null)
                    (activity as ProtocolContainer).quitActivity()
                else
                    activity?.finish()
            }

            override fun onSwipeBottom() {}
            override fun onSwipeLeft() {}
            override fun onSwipeRight() {}
        })
    }

    override fun setColorHalo(color: ColorFilter) {
        val circle = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.halo, null)
        circle?.colorFilter = color
        halo.background = circle

        halo.colorFilter = color
    }

    override fun printHalo(size: Int) {
        halo.layoutParams.width = size
        halo.layoutParams.height = size
        halo.requestLayout()
    }

    override fun hideSystemUI() {
        if (activity == null)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity!!.window.decorView.fitsSystemWindows = false
            activity!!.window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            activity!!.window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            activity!!.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    override fun showSystemUI() {
        if (activity == null)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity!!.window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            activity!!.window.decorView.systemUiVisibility = 0
        }
    }

    override fun onUserInteraction() {
        if (isHaloDisplayed) {
            hideHalo()
            handler.removeCallbacks(displayHaloRunnable)
        } else if (presenter.isHaloOn()) {
            handler.removeCallbacks(displayHaloRunnable)
            handler.postDelayed(displayHaloRunnable, inactivityDuration * 1000)
        }
    }

    private fun displayHalo() {
        if (activity == null)
            return
        isHaloDisplayed = true
        activity!!.window.statusBarColor = resources.getColor(android.R.color.black, resources.newTheme())

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
    }

    private fun hideHalo() {
        if (activity == null)
            return
        isHaloDisplayed = false
        activity!!.window.statusBarColor = resources.getColor(R.color.colorPrimaryDark, resources.newTheme())

        haloBackground.animate().alpha(0f).setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    haloBackground.visibility = View.GONE
                }
            })

        halo.animate().alpha(0f).setDuration(shortAnimationDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    halo.visibility = View.GONE
                    onUserInteraction()
                }
            })
    }

    override fun animateEqualizer(state: Boolean) {
        if (state) {
            equalizer.animateBars()
        } else {
            equalizer.stopBars()
        }
    }

    override fun isMusicPlaying(): Boolean {
        return equalizer.isAnimating
    }

    override fun setPresenter(presenter: ProtocolMenuContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }
}