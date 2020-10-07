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
 *
 * @author Romane Bézier
 */
class AlarmModel : AlarmContract.Model {

    var c : Calendar = Calendar.getInstance()

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
        val pendingIntent = PendingIntent.getBroadcast(context,  AlarmsFragment.id.toInt(), intent, 0)
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
     * @author Romane Bézier
     */
    override fun startAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        val pendingIntent = PendingIntent.getBroadcast(context, AlarmsFragment.id.toInt(), intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        saveAlarm(c.timeInMillis, sharedPreferences)
    }

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
    override fun startAlertWithID(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences, ID: Int) {
        val pendingIntent = PendingIntent.getBroadcast(context,  AlarmsFragment.id.toInt(), intent, 0)
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
     * @param ID Id of the application
     * @author Romane Bézier
     */
    override fun startAlarmWithID(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences, ID: Int) {
        val pendingIntent = PendingIntent.getBroadcast(context, ID, intent, 0)
        val time = sharedPreferences.getLong(ID.toString(), 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        saveAlarmWithID(c.timeInMillis, sharedPreferences, ID)
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     * @param sharedPreferences Shared preferences of the application
     * @author Romane Bézier
     */
    override fun saveAlarm(time: Long, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putLong(AlarmsFragment.id, c.timeInMillis).apply()
        val newId = AlarmsFragment.id.toInt() + 1
        AlarmsFragment.id = newId.toString()
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     * @param sharedPreferences Shared preferences of the application
     * @param ID Id of the application
     * @author Romane Bézier
     */
    override fun saveAlarmWithID(time: Long, sharedPreferences: SharedPreferences, ID: Int) {
        val time = sharedPreferences.getLong(ID.toString(), 0)
        sharedPreferences.edit().remove(ID.toString()).apply()
        sharedPreferences.edit().putLong(ID.toString(), time).apply()
    }

    /**
     * Get time of the alarm
     *
     * @param hourOfDay Hour of the alarm
     * @param minute Minutes of the alarm
     * @return Time in a string
     * @author Romane Bézier
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
     * @author Romane Bézier
     */
    override fun snoozeAlarm(alarmManager: AlarmManager, intent: Intent, context: Context) {
        val pendingIntent = PendingIntent.getBroadcast(context, AlarmsFragment.id.toInt(), intent, 0)

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
     * @param sharedPreferences Shared Preferences of the application
     * @author Romane Bézier
     */
    override fun cancelAlarm(alarmManager: AlarmManager, intent: Intent, context: Context, sharedPreferences: SharedPreferences) {
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
            AlarmReceiver.mp.stop()
    }

    /**
     * Cancel the alarm
     *
     * @param alarmManager Alarm manager of phone
     * @param intent Intent of the activity
     * @param context Context of the activity
     * @param ID Id of the alarm
     * @author Romane Bézier
     */
    override fun cancelAlarmWithID(alarmManager: AlarmManager, intent: Intent, context: Context, ID: Int) {
        val pendingIntent = PendingIntent.getBroadcast(context, ID, intent, 0)
        alarmManager.cancel(pendingIntent)
        if (AlarmReceiver.isMpInitialised() && AlarmReceiver.mp.isPlaying)
            AlarmReceiver.mp.stop()
    }

    /**
     * Load all the saved reminders
     *
     * @param sharedPreferences Shared preferences of the application
     * @return List of reminders
     * @author Romane Bézier
     */
    override fun loadAllReminders(sharedPreferences: SharedPreferences): ArrayList<Long> {
        val reminders = ArrayList<Long>(AlarmsFragment.id.toInt())
        for (i in 0 until AlarmsFragment.id.toInt()) {
            val millis = sharedPreferences.getLong(i.toString(), 0)
            reminders.add(millis)
        }
        return reminders
    }

}