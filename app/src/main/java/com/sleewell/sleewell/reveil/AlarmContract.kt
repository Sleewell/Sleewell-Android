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
         * Get time of the time picker
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minute of the alarm
         * @return Time in a string
         */
        fun getTime(hourOfDay: Int, minute: Int) : String

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @param sharedPreferences Shared preferences of the application
         */
        fun saveAlarm(time: Long, sharedPreferences: SharedPreferences)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         */
        fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences)

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         */
        fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences)

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
         * Get time of the alarm
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minutes of the alarm
         * @return Time in a string
         */
        fun getTime(hourOfDay: Int, minute: Int): String

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @param sharedPreferences Shared preferences of the application
         */
        fun saveAlarm(time: Long, sharedPreferences: SharedPreferences)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         */
        fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences)

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         */
        fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences)

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
