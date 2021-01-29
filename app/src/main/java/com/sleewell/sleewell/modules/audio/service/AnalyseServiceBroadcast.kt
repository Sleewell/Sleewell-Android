package com.sleewell.sleewell.modules.audio.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AnalyseServiceBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val txt = if (intent == null || intent.action == null) "No action" else intent.action
        Log.d("ServiceBroadcast", txt!!)

        with(Intent(context, AnalyseService::class.java)) {
            action = intent?.action
            context?.startService(this)
        }
    }
}