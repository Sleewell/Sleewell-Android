package com.sleewell.sleewell.halo.View

import android.graphics.ColorFilter
import android.os.Bundle
import android.view.View.*
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.halo.MainContract
import com.sleewell.sleewell.halo.Presenter.HaloPresenter


/**
 * This class is the view for the halo
 * display the halo with its action
 *
 * @author gabin warnier de wailly
 */
class HaloActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var haloImage: ImageView
    private lateinit var start20min: Button
    private lateinit var start8min: Button
    private lateinit var stopButton: Button
    private lateinit var colorButton: Button

    /**
     * This method call every initialization for the view
     *
     * @param savedInstanceState
     * @author gabin warnier de wailly
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halo)

        setPresenter(HaloPresenter(this, this))
        InitActivityWidgets()
        presenter.onViewCreated()
        hideSystemUI()
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

        this.start8min.setOnClickListener { presenter.startProtocol(48) }
        this.start20min.setOnClickListener { presenter.startProtocol(120) }
        this.stopButton.setOnClickListener { presenter.stopProtocol() }
        this.colorButton.setOnClickListener { presenter.openDialog() }

        haloImage.background = this.getDrawable(R.drawable.halo)
    }

    /**
     * the method set the color of the halo
     *
     * @param color color rgb for the halo
     * @author gabin warnier de wailly
     */
    override fun setColorHalo(color: ColorFilter) {
        val circle = this.getDrawable(R.drawable.halo)
        circle?.colorFilter = color
        haloImage.background = circle
    }

    /**
     * This method display the halo with the size give in param
     *
     * @param size size of the the halo
     * @author gabin warnier de wailly
     */
    override fun printHalo(size: Int) {
        haloImage.layoutParams.width = size
        haloImage.layoutParams.height = size
        haloImage.requestLayout()
    }

    /**
     * This method save the presenter in the class
     *
     * @param presenter of the current view
     * @author gabin warnier de wailly
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    /**
     * This method hide the system UI for android
     *
     * @author gabin warnier de wailly
     */
    override fun hideSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LOW_PROFILE or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
