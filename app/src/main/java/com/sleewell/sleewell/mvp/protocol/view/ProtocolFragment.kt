package com.sleewell.sleewell.mvp.protocol.view

import android.graphics.ColorFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter


/**
 * A simple [Fragment] subclass.
 */
class ProtocolFragment : Fragment(), ProtocolContract.View {
    //Context
    private lateinit var presenter: ProtocolContract.Presenter
    private lateinit var root: View

    //widgets
    private lateinit var haloImage: ImageView
    private lateinit var start20min: Button
    private lateinit var start8min: Button
    private lateinit var stopButton: Button
    private lateinit var colorButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.new_fragment_protocol, container, false)
        this.enableAutoLock(false)
        initActivityWidgets()
        setPresenter(ProtocolPresenter(this, this.activity as AppCompatActivity))

        return root;
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        //get widgets
        this.haloImage = root.findViewById(R.id.halo)

        this.start8min = root.findViewById(R.id.buttonStart8minutes)
        this.start20min = root.findViewById(R.id.buttonStart20minutes)
        this.stopButton = root.findViewById(R.id.buttonStop)
        this.colorButton = root.findViewById(R.id.buttonColor)

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

        haloImage.background = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.halo, null)
    }

    override fun enableAutoLock(value: Boolean) {
        if (value)
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app close auto
        else
            activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app never close
    }

    override fun setColorHalo(color: ColorFilter) {
        val circle = ResourcesCompat.getDrawable(activity!!.resources, R.drawable.halo, null)
        circle?.colorFilter = color
        haloImage.background = circle
    }

    override fun printHalo(size: Int) {
        haloImage.layoutParams.width = size
        haloImage.layoutParams.height = size
        haloImage.requestLayout()
    }

    override fun hideSystemUI() {
        activity!!.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
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
}