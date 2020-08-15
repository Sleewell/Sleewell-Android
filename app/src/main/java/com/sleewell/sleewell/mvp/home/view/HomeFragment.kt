package com.sleewell.sleewell.mvp.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)
        initActivityWidgets()
        setPresenter(HomePresenter(this, root.context))

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

        //init event listeners
        btnNfc.setOnClickListener {
            startActivity(
                Intent(
                    root.context,
                    ProtocolActivity::class.java
                )
            )
        }
    }

    /**
     * Function called when the screen reappeared on this activity when quiting temporally the app
     */
    override fun onResume() {
        super.onResume()
        presenter.onViewCreated()
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