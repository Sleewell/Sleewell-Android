package com.sleewell.sleewell.mvp.help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cuberto.liquid_swipe.LiquidPager
import com.sleewell.sleewell.R

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        val pager = findViewById<LiquidPager>(R.id.pager)
        pager.adapter = OnBoardingAdapter(supportFragmentManager)
    }
}