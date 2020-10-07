package com.sleewell.sleewell.reveil

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.sleewell.sleewell.reveil.global.BasePresenter
import com.sleewell.sleewell.reveil.global.BaseView
import java.util.ArrayList

/**
 * Contract that defines all the functions of the alarm that will interact with the user
 * @author Romane Bézier
 */
interface AlarmContract {

    interface Model {
        /**
         * Get time of the time picker
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minute of the alarm
         * @return Time in a string
         * @author Romane Bézier
         */
        fun getTime(hourOfDay: Int, minute: Int): String

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun saveAlarm(time: Long, sharedPreferences: SharedPreferences)

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @param sharedPreferences Shared preferences of the application
         * @param ID Id of the application
         * @author Romane Bézier
         */
        fun saveAlarmWithID(time: Long, sharedPreferences: SharedPreferences, ID: Int)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun startAlarm(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun startAlert(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @param ID Id of the application
         * @author Romane Bézier
         */
        fun startAlarmWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences,
            ID: Int
        )

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @param ID Id of the application
         * @author Romane Bézier
         */
        fun startAlertWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences,
            ID: Int
        )

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @author Romane Bézier
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)

        /**
         * Cancel the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared Preferences of the application
         * @author Romane Bézier
         */
        fun cancelAlarm(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Cancel the alarm with ID
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param ID Id of the alarm
         * @author Romane Bézier
         */
        fun cancelAlarmWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            ID: Int
        )

        /**
         * Load all the saved reminders
         *
         * @param sharedPreferences Shared preferences of the application
         * @return List of reminders
         * @author Romane Bézier
         */
        fun loadAllReminders(sharedPreferences: SharedPreferences): ArrayList<Long>

        interface OnFinishedListener {
            //fun onFinished(weather : ApiResult)
            fun onFailure(t: Throwable)
        }
    }

    interface Presenter : BasePresenter {
        /**
         * When view is created
         *
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun onViewCreated(sharedPreferences: SharedPreferences)

        /**
         * Get time of the alarm
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minutes of the alarm
         * @return Time in a string
         * @author Romane Bézier
         */
        fun getTime(hourOfDay: Int, minute: Int): String

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun saveAlarm(time: Long, sharedPreferences: SharedPreferences)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun startAlarm(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun startAlert(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @param ID Id of the alarm
         * @author Romane Bézier
         */
        fun startAlarmWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences,
            ID: Int
        )

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @param ID Id of the alarm
         * @author Romane Bézier
         */
        fun startAlertWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences,
            ID: Int
        )

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @author Romane Bézier
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context)

        /**
         * Cancel the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param sharedPreferences Shared preferences of the application
         * @author Romane Bézier
         */
        fun cancelAlarm(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            sharedPreferences: SharedPreferences
        )

        /**
         * Cancel the alarm with ID
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param ID Id of the alarm
         * @author Romane Bézier
         */
        fun cancelAlarmWithID(
            alarmManager: AlarmManager,
            intent: Intent,
            context: Context,
            ID: Int
        )
    }

    interface View : BaseView<Presenter> {
        /**
         * Show message in toast
         *
         * @param msg Message to display
         * @author Romane Bézier
         */
        fun showToast(msg: String)

        /**
         * Display all the reminders
         *
         * @param reminderList List of reminders
         * @author Romane Bézier
         */
        fun displayReminders(reminderList: ArrayList<Long>)

        /**
         * Snooze the alarm
         *
         * @author Romane Bézier
         */
        fun snoozeAlarm()

        /**
         * Cancel the alarm
         *
         * @author Romane Bézier
         */
        fun cancelAlarm()

        /**
         * Cancel the alarm
         *
         * @param ID Id of the alarm
         */
        fun cancelAlarmWithID(ID: Int)

        /**
         * Start the time picker
         *
         * @author Romane Bézier
         */
        fun launchTimePicker()
    }
}