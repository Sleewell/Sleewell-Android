package com.sleewell.sleewell.reveil

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.sleewell.sleewell.reveil.global.BasePresenter
import com.sleewell.sleewell.reveil.global.BaseView
import java.util.ArrayList

interface AlarmContract {

    interface Model {

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)

        /**
         * Cancel the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         */
        fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)

        /**
         * Load all the saved reminders
         *
         * @param sharedPreferences Shared preferences of the application
         * @return List of reminders
         */
        fun loadAllReminders(sharedPreferences: SharedPreferences): ArrayList<Long>

        interface OnFinishedListener {
            //fun onFinished(weather : ApiResult)
            fun onFailure(t : Throwable)
        }
    }

    interface Presenter : BasePresenter {
        /**
         * When view is created
         *
         * @param sharedPreferences Shared preferences of the application
         */
        fun onViewCreated(sharedPreferences: SharedPreferences)

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)

        /**
         * Cancel the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         */
        fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)
    }

    interface View : BaseView<Presenter> {
        /**
         * Show message in toast
         *
         * @param msg Message to display
         */
        fun showToast(msg: String)

        /**
         * Display all the reminders
         *
         * @param reminderList List of reminders
         */
        fun displayReminders(reminderList: ArrayList<Long>)

        /**
         * Snooze the alarm
         *
         */
        fun snoozeAlarm()

        /**
         * Cancel the alarm
         *
         */
        fun cancelAlarm()
    }
}
