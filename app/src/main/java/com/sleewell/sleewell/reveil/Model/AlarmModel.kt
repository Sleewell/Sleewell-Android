package com.sleewell.sleewell.reveil.Model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.format.DateUtils
import android.text.format.Time
import android.util.Log
import com.sleewell.sleewell.nav.alarms.AlarmsFragment
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.AlarmContract
import java.text.SimpleDateFormat
import java.util.*

/**
 * Alarm Model for the Alarm activity
 *
 */
class AlarmModel : AlarmContract.Model {

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
        sharedPreferences.edit().putLong(AlarmsFragment.id, c.timeInMillis).apply()
        val newId = AlarmsFragment.id.toInt() + 1
        AlarmsFragment.id = newId.toString()
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

    /**
     * Snooze the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     */
    override fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context) {
        val pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0)

        val currentTimeMillis = System.currentTimeMillis()
        val nextUpdateTimeMillis = currentTimeMillis + 1 * DateUtils.MINUTE_IN_MILLIS
        val nextUpdateTime = Time()
        nextUpdateTime.set(nextUpdateTimeMillis)

        alarmManager.setExact(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent)
        AlarmsFragment.instance.saveAlarm(nextUpdateTimeMillis)
    }

    /**
     * Cancel the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     */
    override fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
            AlarmReceiver.mp.stop()
    }

    /**
     * Load all the saved reminders
     *
     * @param sharedPreferences Shared preferences of the application
     * @return List of reminders
     */
    override fun loadAllReminders(sharedPreferences: SharedPreferences): ArrayList<Long> {
        val reminders = ArrayList<Long>(AlarmsFragment.id.toInt())
        Log.d("DEBUUUG", AlarmsFragment.id)
        for (i in 1..AlarmsFragment.id.toInt()) {
            val millis = sharedPreferences.getLong(i.toString(), 0)
            reminders.add(millis)
        }
        Log.d("DEBUUUG", reminders.toString())
        return reminders
    }

}