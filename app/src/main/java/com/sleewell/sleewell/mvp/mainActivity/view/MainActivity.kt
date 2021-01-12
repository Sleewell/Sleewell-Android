package com.sleewell.sleewell.mvp.mainActivity.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.mainActivity.MainContract
import com.sleewell.sleewell.mvp.mainActivity.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var presenter : MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_main)
        setPresenter(MainPresenter(this, this))
        presenter.onViewCreated()
    }

    /**
     * Set the presenter inside the class
     *
     * @param presenter
     * @author Hugo Berthomé
     */
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }
}