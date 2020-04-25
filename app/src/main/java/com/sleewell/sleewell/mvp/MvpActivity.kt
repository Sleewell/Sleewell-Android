package com.sleewell.sleewell.mvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.Model.DependencyInjectorImpl
import com.sleewell.sleewell.mvp.Model.Weather.WeatherState

class MvpActivity : AppCompatActivity(), MainContract.View {
    private lateinit var imageView: ImageView
    private lateinit var button: Button

    // 2
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)

        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)

        // 3
        setPresenter(MainPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()

        // 4
        button.setOnClickListener { presenter.onLoadWeatherTapped() }
    }

    // 5
    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    // 6
    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    // 7
    override fun displayWeatherState(weatherState: WeatherState) {
        val drawable = resources.getDrawable(weatherDrawableResId(weatherState),
            applicationContext.theme
        )
        this.imageView.setImageDrawable(drawable)
    }

    private fun weatherDrawableResId(weatherState: WeatherState) : Int {
        return when (weatherState) {
            WeatherState.SUN -> R.drawable.ic_sun
            WeatherState.RAIN -> R.drawable.ic_umbrella
        }
    }
}