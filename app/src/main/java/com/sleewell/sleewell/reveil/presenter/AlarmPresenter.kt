package com.sleewell.sleewell.reveil.presenter

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import com.sleewell.sleewell.reveil.model.AlarmModel

class AlarmPresenter(view: AlarmContract.View) : AlarmContract.Presenter {

    private var model: AlarmContract.Model = AlarmModel(this)
    private var view: AlarmContract.View? = view

    /**
     * On the destroy of the presenter.
     *
     * @author Romane Bézier
     */
    override fun onDestroy() {
        this.view = null
    }

    /**
     * When view is created.
     *
     * @author Romane Bézier
     */
    override fun onViewCreated() {

    }

    /**
     * Update the alarm.
     *
     * @param updateAlarm Alarm to update.
     * @param mAlarmViewModel View model of the alarm.
     * @author Romane Bézier
     */
    override fun updateAlarm(updateAlarm: Alarm, mAlarmViewModel: AlarmViewModel) {
        model.updateAlarm(updateAlarm, mAlarmViewModel)
    }

    /**
     * Delete the alarm.
     *
     * @param mAlarmViewModel View model of the alarm.
     * @param alarm Current alarm.
     * @author Romane Bézier
     */
    override fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm) {
        model.deleteAlarm(mAlarmViewModel, alarm)
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
    override fun saveAlarm(time: Long, mAlarmViewModel: AlarmViewModel, lifecycleOwner: LifecycleOwner, days: List<Boolean>, ringtone: Uri, vibrate: Boolean, label: String, index: Int, displayed: Boolean) {
        model.saveAlarm(time,mAlarmViewModel, lifecycleOwner, days, ringtone, vibrate, label, index, displayed)
    }

    /**
     * Start the new alarm.
     *
     * @param alarm Current alarm.
     * @author Romane Bézier
     */
    override fun startNewAlarm(alarm: Alarm) {
        view?.startAlarm(alarm, false)
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
    override fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm, restart: Boolean) {
        model.startAlarm(alarmManager, intent, context, alarm, restart)
    }

    /**
     * Start the alert.
     *
     * @param alarmManager Alarm manager of phone.
     * @param intent Intent of the activity.
     * @param context Context of the activity.
     * @param alarm Current alarm.
     * @author Romane Bézier
     */
    override fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm) {
        model.startAlert(alarmManager, intent, context, alarm)
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
    override fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.snoozeAlarm(alarmManager, intent, context, currentAlarm)
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
    override fun stopAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.stopAlarm(alarmManager, intent, context, currentAlarm)
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
    override fun stopAlert(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.stopAlert(alarmManager, intent, context, currentAlarm)
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
        return model.getTime(hourOfDay, minute)
    }
}