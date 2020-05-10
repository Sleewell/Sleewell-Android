package com.sleewell.sleewell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sleewell.sleewell.halo.View.HaloActivity
import com.sleewell.sleewell.mvp.View.MvpActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMvp = findViewById<Button>(R.id.button_mvp)
        buttonMvp.setOnClickListener {
            startActivity(Intent(this, MvpActivity::class.java))
        }

        val buttonHalo = findViewById<Button>(R.id.button_halo)
        buttonHalo.setOnClickListener {
            startActivity(Intent(this, HaloActivity::class.java))
        }
    }
}
