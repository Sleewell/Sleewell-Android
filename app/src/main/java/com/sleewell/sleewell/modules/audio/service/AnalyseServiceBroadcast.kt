package com.sleewell.sleewell.modules.audio.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Broadcast intent for stopping the analyse by clicking on the notification
 *
 * @author Hugo Berthomé
 */
class AnalyseServiceBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val txt = if (intent == null || intent.action == null) "No action" else intent.action
        Log.d("ServiceBroadcast", txt!!)

        if (AnalyseServiceTracker.getServiceState(context) == AnalyseServiceTracker.ServiceState.STARTED) {
            with(Intent(context, AnalyseService::class.java)) {
                action = intent?.action
                context?.startService(this)
            }
        }
    }
}