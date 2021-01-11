package com.sleewell.sleewell.reveil

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import com.sleewell.sleewell.reveil.global.BasePresenter
import com.sleewell.sleewell.reveil.global.BaseView

/**
 * Contract that defines all the functions of the alarm that will interact with the user
 * @author Romane Bézier
 */
interface AlarmContract {

    interface Model {
        /**
         * Update the alarm
         *
         * @param updateAlarm Alarm to update
         * @param mAlarmViewModel View model of the alarm
         * @author Romane Bézier
         */
        fun updateAlarm(updateAlarm: Alarm, mAlarmViewModel: AlarmViewModel)

        /**
         * Delete the alarm
         *
         * @param mAlarmViewModel View model of the alarm
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm)

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @author Romane Bézier
         */
        fun saveAlarm(time: Long, mAlarmViewModel: AlarmViewModel)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm)

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm)

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Stop the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun stopAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Stop the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun stopAlert(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Get time of the alarm
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minutes of the alarm
         * @return Time in a string
         * @author Romane Bézier
         */
        fun getTime(hourOfDay: Int, minute: Int): String
    }

    interface Presenter : BasePresenter {
        /**
         * When view is created
         *
         * @author Romane Bézier
         */
        fun onViewCreated()

        /**
         * Update the alarm
         *
         * @param updateAlarm Alarm to update
         * @param mAlarmViewModel View model of the alarm
         * @author Romane Bézier
         */
        fun updateAlarm(updateAlarm: Alarm, mAlarmViewModel: AlarmViewModel)


        /**
         * Delete the alarm
         *
         * @param mAlarmViewModel View model of the alarm
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm)

        /**
         * Save the alarm
         *
         * @param time Time of the alarm
         * @author Romane Bézier
         */
        fun saveAlarm(time: Long, mAlarmViewModel: AlarmViewModel)

        /**
         * Start the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm)

        /**
         * Start the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param alarm Current alarm
         * @author Romane Bézier
         */
        fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, alarm: Alarm)

        /**
         * Snooze the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Stop the alarm
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun stopAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Stop the alert
         *
         * @param alarmManager Alarm manager of phone
         * @param intent Intent of the activity
         * @param context Context of the activity
         * @param currentAlarm Current alarm
         * @author Romane Bézier
         */
        fun stopAlert(alarmManager: AlarmManager, intent: Intent, context: Context, currentAlarm: Alarm)

        /**
         * Get time of the alarm
         *
         * @param hourOfDay Hour of the alarm
         * @param minute Minutes of the alarm
         * @return Time in a string
         * @author Romane Bézier
         */
        fun getTime(hourOfDay: Int, minute: Int): String
    }

    interface View : BaseView<Presenter> {

        /**
         * Launch the time picker to update the alarm
         *
         * @param currentAlarm Alarm to update
         * @author Romane Bézier
         */
        fun launchTimePickerUpdate(currentAlarm: Alarm)

        /**
         * Convert the time to String
         *
         * @param milliSeconds Time to convert
         * @return Time in a String
         * @author Romane Bézier
         */
        fun convertTime(milliSeconds: Long): String?

        /**
         * Start the alarm
         *
         * @param currentAlarm Alarm to start
         * @author Romane Bézier
         */
        fun startAlarm(currentAlarm: Alarm)

        /**
         * Snooze the alarm
         *
         * @param currentAlarm Alarm to snooze
         * @author Romane Bézier
         */
        fun snoozeAlarm(currentAlarm: Alarm)

        /**
         * Stop the alarm
         *
         * @param currentAlarm Alarm to stop
         * @author Romane Bézier
         */
        fun stopAlarm(currentAlarm: Alarm)

        /**
         * Delete the alarm
         *
         * @param currentAlarm Alarm to delete
         * @author Romane Bézier
         */
        fun deleteAlarm(currentAlarm: Alarm)
    }
}