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
 */
class AlarmPresenter(view: AlarmContract.View) : AlarmContract.Presenter,
        AlarmContract.Model.OnFinishedListener {

    private var model: AlarmContract.Model = AlarmModel()
    private var view: AlarmContract.View? = view

    /**
     * On the destroy of the presenter
     *
     */
    override fun onDestroy() {
        this.view = null
    }

    /**
     * When view is created
     *
     * @param sharedPreferences Shared preferences of the application
     */
    override fun onViewCreated(sharedPreferences: SharedPreferences) {
        val reminderList: ArrayList<Long> = model.loadAllReminders(sharedPreferences)
        view?.displayReminders(reminderList)
    }

    /**
     * Snooze the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
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
     */
    override fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context) {
        model.cancelAlarm(alarmManager, intent, context)
    }

    override fun onFailure(t: Throwable) {
        if (t.message != null)
            view?.showToast(t.message!!)
        else
            view?.showToast("An error occurred")
    }
}