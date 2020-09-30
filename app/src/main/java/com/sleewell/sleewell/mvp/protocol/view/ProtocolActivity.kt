package com.sleewell.sleewell.mvp.protocol.view

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.protocol.ProtocolContract
import com.sleewell.sleewell.mvp.protocol.presenter.ProtocolPresenter

class ProtocolActivity : AppCompatActivity(), ProtocolContract.View {
    private lateinit var presenter: ProtocolContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol)
        this.enableAutoLock(false)

        setPresenter(ProtocolPresenter(this, this))
    }

    override fun onDestroy() {
        super.onDestroy()
        this.enableAutoLock(true)
    }

    override fun enableAutoLock(value: Boolean) {
        if (value)
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app close auto
        else
            this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // the app never close
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
