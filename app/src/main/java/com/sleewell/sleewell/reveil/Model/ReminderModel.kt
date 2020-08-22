package com.sleewell.sleewell.reveil.Model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.sleewell.sleewell.reveil.ReminderContract
import com.sleewell.sleewell.reveil.View.ReminderActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Reminder Model for the Reminder activity
 *
 */
class ReminderModel : ReminderContract.Model {

    companion object {
        lateinit var c : Calendar
    }

    /**
     * Start the alert
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param sharedPreferences Shared preferences of the application
     */
    override fun startAlert(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        c.add(Calendar.HOUR, -8)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    /**
     * Start the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param sharedPreferences Shared preferences of the application
     */
    override fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        saveAlarm(c.timeInMillis, sharedPreferences)
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     * @param sharedPreferences Shared preferences of the application
     */
    override fun saveAlarm(time: Long, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putLong(ReminderActivity.id, c.timeInMillis).apply()
        val newId = ReminderActivity.id.toInt() + 1
        ReminderActivity.id = newId.toString()
    }

    /**
     * Get time of the alarm
     *
     * @param hourOfDay Hour of the alarm
     * @param minute Minutes of the alarm
     * @return Time in a string
     */
    override fun getTime(hourOfDay: Int, minute: Int) : String {
        c = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = hourOfDay
        c[Calendar.MINUTE] = minute
        c[Calendar.SECOND] = 0

        val date = Date(c.time.toString())
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("GMT+2")

        return formatter.format(date)
    }
}