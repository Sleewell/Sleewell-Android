package com.sleewell.sleewell.reveil.model
import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.*
import androidx.lifecycle.LifecycleOwner
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Alarm Model for the Alarm activity.
 *
 * @author Romane Bézier
 */
class AlarmModel(presenter: AlarmContract.Presenter) : AlarmContract.Model {

    private var presenter: AlarmContract.Presenter? = presenter
    var c : Calendar = Calendar.getInstance()

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
        displayed: Boolean
    ) {
        val alarm = Alarm(0, time, false, days, ringtone.toString(), vibrate, label, displayed)
        if (index == 0) {
            mAlarmViewModel.addAlarm(alarm).observe(lifecycleOwner, { id ->
                mAlarmViewModel.getById(id.toInt()).observe(lifecycleOwner, { alarm ->
                    presenter?.startNewAlarm(alarm)
                })
            })
        } else {
            presenter?.startNewAlarm(alarm)
        }
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
        alarm: Alarm
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setAlarmClock(
            AlarmClockInfo(c.timeInMillis, pendingIntent),
            pendingIntent
        )

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
        c.add(Calendar.HOUR, -8)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)

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
        @Suppress("DEPRECATION")
        val nextUpdateTime = Time()
        @Suppress("DEPRECATION")
        nextUpdateTime.set(nextUpdateTimeMillis)

        alarmManager.setAlarmClock(
            AlarmClockInfo(c.timeInMillis, pendingIntent),
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
        currentAlarm: Alarm
    ) {
        val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
        alarmManager.cancel(pendingIntent)
        if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
            AlarmReceiver.mp.stop()
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
        currentAlarm: Alarm
    ) {
        val pendingIntent = PendingIntent.getBroadcast(context, currentAlarm.id, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Get time of the alarm.
     *
     * @param hourOfDay Hour of the alarm.
     * @param minute Minutes of the alarm.
     * @return Time in a string.
     * @author Romane Bézier
     */
    override fun getTime(hourOfDay: Int, minute: Int) : String {
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