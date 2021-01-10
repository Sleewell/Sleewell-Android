package com.sleewell.sleewell.mvp.protocol.view

import android.content.Intent
import android.graphics.ColorFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.gesturelistener.OnSwipeListener
import com.sleewell.sleewell.mvp.protocol.ProtocolMenuContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolMenuPresenter
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter
import es.claucookie.miniequalizerlibrary.EqualizerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProtocolMenuFragment : Fragment(), ProtocolMenuContract.View {
    //Context
    private lateinit var presenter: ProtocolMenuContract.Presenter
    private lateinit var root: View

    //widgets
    private lateinit var navController: NavController

    private lateinit var dateView: TextView
    private lateinit var musicButton: Button
    private lateinit var equalizer: EqualizerView
    private lateinit var container: ConstraintLayout

    private lateinit var halo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.new_fragment_protocol_menu, container, false)
        initActivityWidgets()
        setPresenter(ProtocolMenuPresenter(this, this.activity as AppCompatActivity))

        return root
    }

    override fun initActivityWidgets() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_main)

        dateView = root.findViewById(R.id.date_protoc)
        musicButton = root.findViewById(R.id.musicButton)
        equalizer = root.findViewById<View>(R.id.equalizer_view) as EqualizerView
        container = root.findViewById(R.id.protocol_layout)
        halo =  root.findViewById(R.id.halo)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateView.text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d LLL"))
        }

        musicButton.setOnClickListener { presenter.pauseMusic() }

        container.setOnTouchListener(object : OnSwipeListener(root.context) {
            override fun onSwipeTop() {
                // navigate to menu
                navController.navigate(R.id.action_protocolMenuFragment_to_menuFragment)
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
        activity!!.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
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
}