package com.sleewell.sleewell.halo.View

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.os.CountDownTimer
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
import kotlinx.android.synthetic.main.activity_halo.*
import kotlinx.android.synthetic.main.colorpicker.*


class HaloActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var haloImage : ImageView
    private lateinit var startButton : Button
    private lateinit var stopButton : Button
    private lateinit var colorButton: Button
    private lateinit var mImageView: ImageView
    private lateinit var mResultView: ImageView
    private lateinit var bitmap: Bitmap

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

        this.startButton = findViewById(R.id.buttonStart)
        this.stopButton = findViewById(R.id.buttonStop)
        this.colorButton = findViewById(R.id.buttonColor)
        this.startButton.setOnClickListener{
            presenter.startProtocol()
        }
        this.stopButton.setOnClickListener{
            presenter.stopProtocol()
        }
        this.colorButton.setOnClickListener{
            presenter.openDialog()
        }
        haloImage.background = this.getDrawable(R.drawable.halo)
    }

    override fun setColorHalo(color: ColorFilter) {
        val circle = this.getDrawable(R.drawable.halo)
        circle?.colorFilter = color
        haloImage.background = circle
    }

    override fun printHalo(size: Int) {
        haloImage.layoutParams.width = size
        haloImage.layoutParams.height = size
        haloImage.requestLayout()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun hideSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LOW_PROFILE or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
