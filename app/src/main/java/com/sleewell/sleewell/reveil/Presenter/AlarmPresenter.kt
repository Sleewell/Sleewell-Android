package com.sleewell.sleewell.reveil.Presenter

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.Model.AlarmModel

/**
 * Alarm Presenter for the Alarm activity
 *
 * @param view View of the presenter
 * @author Romane Bézier
 */
class AlarmPresenter(view: AlarmContract.View) : AlarmContract.Presenter,
        AlarmContract.Model.OnFinishedListener {

    private var model: AlarmContract.Model = AlarmModel()
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
     * @param sharedPreferences Shared preferences of the application
     * @author Romane Bézier
     */
    override fun onViewCreated(sharedPreferences: SharedPreferences) {
        val reminderList: ArrayList<Long> = model.loadAllReminders(sharedPreferences)
        view?.displayReminders(reminderList)
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

    /**
     * Start the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param sharedPreferences Shared preferences of the application
     * @author Romane Bézier
     */
    override fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        model.startAlarm(alarmManager, intent, context, sharedPreferences)
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     * @param sharedPreferences Shared preferences of the application
     * @author Romane Bézier
     */
    override fun saveAlarm(time: Long, sharedPreferences: SharedPreferences) {
        model.saveAlarm(time, sharedPreferences)
    }

    /**
     * Start the alert
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param sharedPreferences Shared preferences of the application
     * @author Romane Bézier
     */
    override fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        model.startAlert(alarmManager, intent, context, sharedPreferences)
    }

    /**
     * Snooze the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @author Romane Bézier
     */
    override fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context) {
        model.snoozeAlarm(alarmManager, intent, context)
    }

    /**
     * Cancel the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param sharedPreferences Shared Preferences of the application
     * @author Romane Bézier
     */
    override fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        model.cancelAlarm(alarmManager, intent, context, sharedPreferences)
    }

    /**
     * On failure
     *
     * @param t Throwable
     * @author Romane Bézier
     */
    override fun onFailure(t: Throwable) {
        if (t.message != null)
            view?.showToast(t.message!!)
        else
            view?.showToast("An error occurred")
    }
}