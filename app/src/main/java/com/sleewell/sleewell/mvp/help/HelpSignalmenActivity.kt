package com.sleewell.sleewell.mvp.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sleewell.sleewell.BuildConfig
import com.sleewell.sleewell.R


class HelpSignalmenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_signalement)

        val versionCode = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        val versionText = findViewById<TextView>(R.id.textVersion)
        versionText.text = getString(R.string.version_app, versionName, versionCode.toString())

        val buttonSignal = findViewById<Button>(R.id.buttonProblemSignal)
        buttonSignal.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sleewell.fr/?version=$versionName-$versionCode")
                )
            )
        }

        val buttonReturn = findViewById<ImageButton>(R.id.imageReturnHelp)
        buttonReturn.setOnClickListener {
            finish()
        }
    }
}