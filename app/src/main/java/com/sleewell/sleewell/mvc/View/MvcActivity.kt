package com.sleewell.sleewell.mvc.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvc.Controller.Controller
import com.sleewell.sleewell.mvc.Controller.MvcController
import com.sleewell.sleewell.mvp.Presenter.MainPresenter
import com.squareup.picasso.Picasso

class MvcActivity : AppCompatActivity(), View {
    private lateinit var controller: Controller

    private lateinit var imageView: ImageView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)

        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)

        controller = MvcController(this)

        button.setOnClickListener { controller.onLoadWeatherTapped() }
        controller.onViewCreated()
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
