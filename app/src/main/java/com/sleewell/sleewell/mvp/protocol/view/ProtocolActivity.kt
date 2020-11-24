package com.sleewell.sleewell.mvp.protocol.view

import android.graphics.ColorFilter
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter

class ProtocolActivity : AppCompatActivity(), ProtocolContract.View {
    private lateinit var presenter: ProtocolContract.Presenter
    private lateinit var haloImage: ImageView
    private lateinit var start20min: Button
    private lateinit var start8min: Button
    private lateinit var stopButton: Button
    private lateinit var colorButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol)
        this.enableAutoLock(false)

        InitActivityWidgets()
        setPresenter(ProtocolPresenter(this, this))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.enableAutoLock(true)
    }

    /**
     * Function called when quitting the activity
     */
    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: ProtocolContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun enableAutoLock(value: Boolean) {
        if (value)
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app close auto
        else
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app never close
    }

    /**
     * This method init each buttons and setup the color of the halo
     *
     * @author gabin warnier de wailly
     */
    private fun InitActivityWidgets() {
        this.haloImage = findViewById(R.id.halo)

        this.start8min = findViewById(R.id.buttonStart8minutes)
        this.start20min = findViewById(R.id.buttonStart20minutes)
        this.stopButton = findViewById(R.id.buttonStop)
        this.colorButton = findViewById(R.id.buttonColor)

        this.start8min.setOnClickListener {
            hideSystemUI()
            presenter.startProtocol(48)
        }
        this.start20min.setOnClickListener {
            hideSystemUI()
            presenter.startProtocol(120)
        }
        this.stopButton.setOnClickListener { presenter.stopProtocol() }
        this.colorButton.setOnClickListener { presenter.openDialog() }

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

    override fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
