package com.sleewell.sleewell.mvp.protocol.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter

class ProtocolActivity : AppCompatActivity(), ProtocolContract.View {
    private lateinit var presenter: ProtocolContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol)

        setPresenter(ProtocolPresenter(this, this))
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
}
