package com.sleewell.sleewell.reveil.presenter

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import com.sleewell.sleewell.reveil.model.AlarmModel

class AlarmPresenter(view: AlarmContract.View) : AlarmContract.Presenter {

    private var model: AlarmContract.Model = AlarmModel(this)
    private var view: AlarmContract.View? = view

    /**
     * On the destroy of the presenter
     *
     * @author Romane Bézier
     */
    override fun onDestroy() {
        this.view = null
    }

    /**
     * When view is created
     *
     * @author Romane Bézier
     */
    override fun onViewCreated() {

    }

    /**
     * Update the alarm
     *
     * @param updateAlarm Alarm to update
     * @param mAlarmViewModel View model of the alarm
     * @author Romane Bézier
     */
    override fun updateAlarm(updateAlarm: Alarm, mAlarmViewModel: AlarmViewModel) {
        model.updateAlarm(updateAlarm, mAlarmViewModel)
    }

    /**
     * Delete the alarm
     *
     * @param mAlarmViewModel View model of the alarm
     * @param alarm Current alarm
     * @author Romane Bézier
     */
    override fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm) {
        model.deleteAlarm(mAlarmViewModel, alarm)
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     * @author Romane Bézier
     */
    override fun saveAlarm(time: Long, mAlarmViewModel: AlarmViewModel, lifecycleOwner: LifecycleOwner, vibrate: Boolean, label: String) {
        model.saveAlarm(time,mAlarmViewModel, lifecycleOwner, vibrate, label)
    }

    /**
     * Start the new alarm
     *
     * @param alarm Current alarm
     * @author Romane Bézier
     */
    override fun startNewAlarm(alarm: Alarm) {
        view?.startAlarm(alarm)
    }

    /**
     * Start the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param alarm Current alarm
     * @author Romane Bézier
     */
    override fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm) {
        model.startAlarm(alarmManager, intent, context, alarm)
    }

    /**
     * Start the alert
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param alarm Current alarm
     * @author Romane Bézier
     */
    override fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm) {
        model.startAlert(alarmManager, intent, context, alarm)
    }

    /**
     * Snooze the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param currentAlarm Current alarm
     * @author Romane Bézier
     */
    override fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.snoozeAlarm(alarmManager, intent, context, currentAlarm)
    }

    /**
     * Stop the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param currentAlarm Current alarm
     * @author Romane Bézier
     */
    override fun stopAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.stopAlarm(alarmManager, intent, context, currentAlarm)
    }

    /**
     * Stop the alert
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param currentAlarm Current alarm
     * @author Romane Bézier
     */
    override fun stopAlert(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm) {
        model.stopAlert(alarmManager, intent, context, currentAlarm)
    }

    /**
     * Get time of the alarm
     *
     * @param hourOfDay Hour of the alarm
     * @param minute Minutes of the alarm
     * @return Time in a string
     * @author Romane Bézier
     */
    override fun getTime(hourOfDay: Int, minute: Int): String {
        return model.getTime(hourOfDay, minute)
    }
}