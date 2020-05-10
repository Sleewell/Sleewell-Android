package com.sleewell.sleewell.halo.View

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.halo.MainContract
import com.sleewell.sleewell.halo.Presenter.HaloPresenter


class HaloActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var haloImage : ImageView
    private lateinit var upButton : Button
    private lateinit var downButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halo)

        setPresenter(HaloPresenter(this, this))
        InitActivityWidgets()
        presenter.onViewCreated()
        hideSystemUI()
    }

    private fun InitActivityWidgets()
    {
        this.haloImage = findViewById(R.id.halo)
        this.upButton = findViewById(R.id.buttonMore)
        this.downButton= findViewById(R.id.buttonLess)
        this.upButton.setOnClickListener {
            presenter.buttonMore()
            presenter.onViewCreated()
        }
        this.downButton.setOnClickListener {
            presenter.buttonLess()
            presenter.onViewCreated()
        }
    }

    override fun printHalo(size: Int) {
        haloImage.layoutParams.width = size
        haloImage.layoutParams.height = size
        haloImage.requestLayout()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LOW_PROFILE or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
