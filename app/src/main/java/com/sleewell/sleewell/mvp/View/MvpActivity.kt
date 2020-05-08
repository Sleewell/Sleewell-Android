package com.sleewell.sleewell.mvp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.MainContract
import com.sleewell.sleewell.mvp.Presenter.MainPresenter
import com.squareup.picasso.Picasso

class MvpActivity : AppCompatActivity(), MainContract.View {
    private lateinit var imageView: ImageView
    private lateinit var button: Button

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)

        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)

        setPresenter(MainPresenter(this))
        presenter.onViewCreated()

        button.setOnClickListener { presenter.onLoadWeatherTapped() }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayWeatherState(imageUrl: String) {
        this.imageView.isEnabled = true

        Picasso.with(this).load(imageUrl).into(this.imageView)
    }

    override fun displayWaitingState() {
        this.imageView.isEnabled = false
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}