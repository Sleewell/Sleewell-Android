package com.sleewell.sleewell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sleewell.sleewell.mvc.MvcActivity
import com.sleewell.sleewell.mvp.MvpActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMvp = findViewById<Button>(R.id.button_mvp)
        buttonMvp.setOnClickListener {
            startActivity(Intent(this, MvpActivity::class.java))
        }

        val buttonMvc = findViewById<Button>(R.id.button_mvc)
        buttonMvc.setOnClickListener {
            startActivity(Intent(this, MvcActivity::class.java))
        }
    }
}
