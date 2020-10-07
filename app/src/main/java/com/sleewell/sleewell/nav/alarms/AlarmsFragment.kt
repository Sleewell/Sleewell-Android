package com.sleewell.sleewell.nav.alarms

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmAdapter
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.AlertReceiver
import com.sleewell.sleewell.reveil.Presenter.AlarmPresenter
import kotlinx.android.synthetic.main.layout_reminder_row.*
import java.util.*

interface CellClickListener {
    fun launchTimePickerWithID(ID: Int)
    fun cancelAlarmWithIDCheckBox(ID: Int)
    fun startAlarmCheckBox(ID: Int)
}

class AlarmsFragment : Fragment(), AlarmContract.View, CellClickListener {

    companion object {
        lateinit var instance: AlarmsFragment
            private set
        var id = "0"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoReminders: TextView
    private lateinit var alarmAdapter: AlarmAdapter

    private lateinit var presenter: AlarmContract.Presenter

    /**
     * When view is created
     *
     * @param savedInstanceState Save of the instance state
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        instance = this

        val root = inflater.inflate(R.layout.fragment_alarms, container, false)
        val fabCreateReminder: FloatingActionButton = root.findViewById(R.id.fabCreateReminder)
        fabCreateReminder.setOnClickListener {
            launchTimePicker()
        }
        recyclerView = root.findViewById(R.id.recyclerViewReminders)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        textViewNoReminders = root.findViewById(R.id.textViewNoReminders)
        setPresenter(AlarmPresenter(this))
        presenter.onViewCreated(this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE))
        return root
    }

    /**
     * Show message in toast
     *
     * @param msg Message to display
     */
    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Display all the reminders
     *
     * @param reminderList List of reminders
     */
    override fun displayReminders(reminderList: ArrayList<Long>) {
        if (reminderList.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            textViewNoReminders.visibility = View.GONE
            alarmAdapter = AlarmAdapter(reminderList, this)
            recyclerView.adapter = alarmAdapter
        }
    }

    /**
     * Save the alarm
     *
     * @param time Time of the alarm
     */
    fun saveAlarm(time: Long) {
        presenter.saveAlarm(time, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE))
    }

    /**
     * Snooze the alarm
     *
     */
    override fun snoozeAlarm() {
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        presenter.snoozeAlarm(alarmManager, intent, context!!)
    }

    /**
     * Cancel the alarm
     *
     */
    override fun cancelAlarm() {
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        presenter.cancelAlarm(alarmManager, intent, context!!, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE))
    }

    /**
     * Cancel the alarm
     *
     * @param ID Id of the alarm
     */
    override fun cancelAlarmWithID(ID: Int) {
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        presenter.cancelAlarmWithID(alarmManager, intent, context!!, ID)
    }

    /**
     * Set the presenter of the view
     *
     * @param presenter The presenter
     */
    override fun setPresenter(presenter: AlarmContract.Presenter) {
        this.presenter = presenter
    }

    /**
     * Start the time picker
     *
     * @author Romane Bézier
     */
    override fun launchTimePicker() {
        val date = Calendar.getInstance()
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val minute = date.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { view, hourOfDay, minute ->
            val formatted: String = presenter.getTime(hourOfDay, minute)
            val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentAlarm = Intent(context, AlarmReceiver::class.java)
            presenter.startAlarm(alarmManager, intentAlarm, context!!, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE))
            val intentAlert = Intent(context, AlertReceiver::class.java)
            presenter.startAlert(alarmManager, intentAlert, context!!, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE))
        }, hour, minute, true)
        timePickerDialog.show()
    }

    /**
     * Start the time picker
     *
     * @author Romane Bézier
     */
    override fun launchTimePickerWithID(ID: Int) {
        val date = Calendar.getInstance()
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val minute = date.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { view, hourOfDay, minute ->
            cancelAlarmWithID(ID)
            this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE).edit().remove(ID.toString()).apply()

            val formatted: String = presenter.getTime(hourOfDay, minute)
            val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentAlarm = Intent(context, AlarmReceiver::class.java)
            presenter.startAlarm(
                alarmManager,
                intentAlarm,
                context!!,
                this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE)
            )
            val intentAlert = Intent(context, AlertReceiver::class.java)
            presenter.startAlert(
                alarmManager,
                intentAlert,
                context!!,
                this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE)
            )
        }, hour, minute, true)
        timePickerDialog.show()
    }

    /**
     * Cancel alarm with ID when check box is unchecked
     *
     * @param id
     * @author Romane Bézier
     */
    override fun cancelAlarmWithIDCheckBox(ID: Int) {
        cancelAlarmWithID(ID)
    }

    /**
     * Start alarm with ID when check box is checked
     *
     * @author Romane Bézier
     */
    override fun startAlarmCheckBox(ID: Int) {
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentAlarm = Intent(context, AlarmReceiver::class.java)
        presenter.startAlarmWithID(alarmManager, intentAlarm, context!!, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE), ID)
        val intentAlert = Intent(context, AlertReceiver::class.java)
        presenter.startAlertWithID(alarmManager, intentAlert, context!!, this.activity!!.getSharedPreferences("com.sleewell", Context.MODE_PRIVATE), ID)
    }
}