package com.sleewell.sleewell.mvp.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sleewell.sleewell.mvp.protocol.view.ProtocolActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.home.HomeContract
import com.sleewell.sleewell.mvp.home.presenter.HomePresenter

class HomeFragment : Fragment(), HomeContract.View {
    //Context
    private lateinit var presenter: HomeContract.Presenter
    private lateinit var root: View

    //View widgets
    private lateinit var btnNfc: Button
    private lateinit var btnRecord: Button
    private lateinit var btnPlay: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)
        initActivityWidgets()
        setPresenter(HomePresenter(this, root.context as AppCompatActivity))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    /**
     * Initialise all the widgets from the layout
     */
    private fun initActivityWidgets() {
        //get widgets
        this.btnNfc = root.findViewById(R.id.btn_nfc)
        this.btnRecord = root.findViewById(R.id.btn_record)
        this.btnPlay = root.findViewById(R.id.btn_play)

        //init event listeners
        btnNfc.setOnClickListener {
            startActivity(
                Intent(
                    root.context,
                    ProtocolActivity::class.java
                )
            )
        }

        btnRecord.setOnClickListener {
            presenter.onRecordClick()
        }

        btnPlay.setOnClickListener {
            presenter.onPlayClick()
        }
    }

    /**
     * Function called when the screen reappeared on this activity when quiting temporally the app
     */
    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }

    /**
     * Function called when quitting the activity
     */
    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    /**
     * Display the nfc button on the screen
     *
     * @param state true - display, false - hide
     * @author Hugo Berthomé
     */
    override fun displayNfcButton(state: Boolean) {
        btnNfc.isEnabled = state
        if (state)
            btnNfc.visibility = View.VISIBLE
        else
            btnNfc.visibility = View.GONE
    }

    /**
     * Display if the device is currently recording or not
     *
     * @param state true - display, false - hide
     * @author Hugo Berthomé
     */
    override fun displayRecordState(state: Boolean) {
        if (state) {
           this.btnRecord.text = "Stop"
        } else {
            this.btnRecord.text = "Record"
        }
    }

    /**
     * Display if the device is currently playing the record
     *
     * @param state true - display, false - hide
     * @author Hugo Berthomé
     */
    override fun displayPlayerState(state: Boolean) {
        if (state)
            btnPlay.text = "Stop Sound"
        else
            btnPlay.text = "Play"
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

}