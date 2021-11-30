package com.sleewell.sleewell.reveil.model

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.text.format.Time
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.milliseconds

/**
 * Alarm Model for the Alarm activity.
 *
 * @author Romane Bézier
 */
class AlarmModel(presenter: AlarmContract.Presenter) : AlarmContract.Model {

    private var presenter: AlarmContract.Presenter? = presenter
    var c: Calendar = Calendar.getInstance()

    /**
     * Update the alarm.
     *
     * @param updateAlarm Alarm to update.
     * @param mAlarmViewModel View model of the alarm.
     * @author Romane Bézier
     */
    override fun updateAlarm(updateAlarm: Alarm, mAlarmViewModel: AlarmViewModel) {
        mAlarmViewModel.updateAlarm(updateAlarm)
    }

    /**
     * Delete the alarm.
     *
     * @param mAlarmViewModel View model of the alarm.
     * @param alarm Current alarm.
     * @author Romane Bézier
     */
    override fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm) {
        mAlarmViewModel.deleteAlarm(alarm)
    }

    /**
     * Save the alarm.
     *
     * @param time Time of the alarm
     * @param mAlarmViewModel View model of the alarm.
     * @param lifecycleOwner Lifecycle owner.
     * @param days Days of the alarm.
     * @param ringtone Ringtone of the alarm.
     * @param vibrate Vibration of the alarm.
     * @param label Label of the alarm.
     * @param index Index of the alarm.
     * @param displayed Visibility of the alarm.
     * @param show Visibility of the alarm.
     * @author Romane Bézier
     */
    override fun saveAlarm(
        time: Long,
        mAlarmViewModel: AlarmViewModel,
        lifecycleOwner: LifecycleOwner,
        days: List<Boolean>,
        ringtone: Uri,
        vibrate: Boolean,
        label: String,
        index: Int,
        displayed: Boolean,
        show: Boolean
    ) {
        val copy = mutableListOf(
            false,
            false,
            false,
            false,
            false,
            false,
            false
        )
        copy[index] = true

        val alarm =
            Alarm(0, time, false, copy, ringtone.toString(), vibrate, label, displayed, show)
        mAlarmViewModel.addAlarm(alarm).observe(lifecycleOwner, { id ->
            mAlarmViewModel.getById(id).observe(lifecycleOwner, { alarm ->
                presenter?.startNewAlarm(alarm)
            })
        })
    }

    /**
     * Start the alarm.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param alarm Current alarm.
     * @author Romane Bézier
     */
    override fun startAlarm(
        alarmManager: AlarmManager,
        intent: Intent,
        context: Context,
        alarm: Alarm,
        restart: Boolean
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        c.timeInMillis = alarm.time
        if (alarm.days.contains(true)) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                alarm.time,
                604800000, // 1000 * 60 * 60 * 24 * 7 == 1 week
                pendingIntent
            )
        } else {
            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1)
                alarm.time = c.timeInMillis
            }
            alarmManager.setAlarmClock(
                AlarmClockInfo(c.timeInMillis, pendingIntent),
                pendingIntent
            )
        }
    }

    /**
     * Start the alert.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param alarm Alarm to start.
     * @author Romane Bézier
     */
    override fun startAlert(
        alarmManager: AlarmManager,
        intent: Intent,
        context: Context,
        alarm: Alarm
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        c.timeInMillis = alarm.time
        c.add(Calendar.HOUR, -8)
        if (alarm.days.contains(true)) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                c.timeInMillis,
                604800000,
                pendingIntent
            )
        } else {
            if (!c.before(Calendar.getInstance())) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
            }
        }
    }

    /**
     * Snooze the alarm.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param currentAlarm Current alarm.
     * @author Romane Bézier
     */
    override fun snoozeAlarm(
        alarmManager: AlarmManager,
        intent: Intent,
        context: Context,
        currentAlarm: Alarm
    ) {
        val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)

        val currentTimeMillis = System.currentTimeMillis()
        val nextUpdateTimeMillis = currentTimeMillis + 5 * DateUtils.MINUTE_IN_MILLIS

        alarmManager.setAlarmClock(
            AlarmClockInfo(nextUpdateTimeMillis, pendingIntent),
            pendingIntent
        )
    }

    /**
     * Stop the alarm.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param currentAlarm Current alarm.
     * @author Romane Bézier
     */
    override fun stopAlarm(
        alarmManager: AlarmManager,
        intent: Intent,
        context: Context,
        currentAlarm: Alarm,
        fromNotification: Boolean
    ) {
        if (fromNotification) {
            if (!currentAlarm.days.contains(true)) {
                val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
                alarmManager.cancel(pendingIntent)
            }
            if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
                AlarmReceiver.mp.stop()
        } else {
            val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * Stop the alert.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param currentAlarm Current alarm.
     * @author Romane Bézier
     */
    override fun stopAlert(
        alarmManager: AlarmManager,
        intent: Intent,
        context: Context,
        currentAlarm: Alarm, fromNotification: Boolean
    ) {
        if (fromNotification) {
            if (!currentAlarm.days.contains(true)) {
                val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
                alarmManager.cancel(pendingIntent)
            }
            if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
                AlarmReceiver.mp.stop()
        } else {
            val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * Get time of the alarm.
     *
     * @param hourOfDay Hour of the alarm.
     * @param minute Minutes of the alarm.
     * @return Time in a string.
     * @author Romane Bézier
     */
    override fun getTime(hourOfDay: Int, minute: Int): String {
        c = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = hourOfDay
        c[Calendar.MINUTE] = minute
        c[Calendar.SECOND] = 0

        @Suppress("DEPRECATION")
        val date = Date(c.time.toString())
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        return formatter.format(date)
    }
}