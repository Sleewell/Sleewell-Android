package com.sleewell.sleewell.mvp.openWeather.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.mvp.openWeather.OpenWeatherContract
import com.sleewell.sleewell.mvp.openWeather.presenter.OpenWeatherPresenter

class OpenWeatherActivity : AppCompatActivity(), OpenWeatherContract.View {
    private lateinit var presenter: OpenWeatherContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPresenter(OpenWeatherPresenter(this))

        //button.setOnClickListener { presenter.onLoadWeatherTapped() }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setPresenter(presenter: OpenWeatherContract.Presenter) {
        this.presenter = presenter
        presenter.onViewCreated()
    }

    override fun displayWeatherState(imageUrl: String) {
        //Picasso.with(this).load(imageUrl).into(this.imageView)
    }

    override fun displayWaitingState() {
        //this.imageView.isEnabled = false
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}