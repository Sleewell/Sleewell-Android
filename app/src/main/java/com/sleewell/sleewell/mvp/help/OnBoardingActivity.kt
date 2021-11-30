package com.sleewell.sleewell.mvp.help

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cuberto.liquid_swipe.LiquidPager
import com.sleewell.sleewell.R
import com.sleewell.sleewell.modules.settings.SettingsManager
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val pager = findViewById<LiquidPager>(R.id.pager)
        pager.adapter = OnBoardingAdapter(supportFragmentManager)
    }

    fun dismissActivity() {
        val settings = SettingsManager(this)

        settings.setTutorial(false)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}