package com.sleewell.sleewell.splashScreen

import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import com.sleewell.sleewell.BuildConfig
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.mainActivity.view.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val versionCode = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        val versionText = findViewById<TextView>(R.id.textVersion)
        versionText.text = "$versionName  -  $versionCode"
    }

    override fun onStart() {
        super.onStart()

        val animateSquirrel = findViewById<ImageView>(R.id.iconSplashScreen)
        val animated : Animatable2 = animateSquirrel.drawable as Animatable2
        animated.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                animated.start()
            }
        })
        animated.start()

        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
        timer.start()
    }
}