package com.sleewell.sleewell.mvp.menu.alarm.view

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.AlertReceiver
import com.sleewell.sleewell.reveil.data.ListAdapter
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import com.sleewell.sleewell.reveil.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.new_fragment_alarm.*
import java.text.SimpleDateFormat
import java.util.*


class AlarmsFragment : Fragment(), AlarmContract.View, AdapterView.OnItemSelectedListener {

    private lateinit var mAlarmViewModel: AlarmViewModel
    private lateinit var presenter: AlarmContract.Presenter

    companion object {
        lateinit var instance: AlarmsFragment
    }

    /**
     * When view is created
     *
     * @param savedInstanceState Save of the instance state
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        instance = this
        val root = inflater.inflate(R.layout.new_fragment_alarm, container, false)

        setHasOptionsMenu(true)

        val floatingActionButton: FloatingActionButton = root.findViewById(R.id.add_alarm_button)
        floatingActionButton.setOnClickListener {
            //launchTimePicker()
            changeVisibilityLayouts()
        }

        val toolbarAlarm : Toolbar = root.findViewById(R.id.toolbar_alarm)
        toolbarAlarm.setNavigationOnClickListener {
            changeVisibilityLayouts()
        }

        val spinnerAlarm : Spinner = root.findViewById(R.id.spinner_alarm)
        spinnerAlarm.onItemSelectedListener = this
        val sounds: MutableList<String> = ArrayList()
        sounds.add("First")
        sounds.add("Second")
        sounds.add("Third")

        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, sounds)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAlarm.adapter = dataAdapter

        val adapter = ListAdapter(this)

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView_alarm)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmViewModel.readAllData.observe(viewLifecycleOwner, { alarm ->
            adapter.setData(alarm)
        })

        setPresenter(AlarmPresenter(this))
        return root
    }

    private fun changeVisibilityLayouts() {
        toolbar_alarm.visibility = if (toolbar_alarm.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        time_picker_alarm.visibility = if (time_picker_alarm.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        repeat_text.visibility = if (repeat_text.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        recyclerView_alarm.visibility = if (recyclerView_alarm.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        if (add_alarm_button.visibility == View.VISIBLE) {
            add_alarm_button.hide()
        } else {
            add_alarm_button.show()
        }
    }

    /**
     * Launch the time picker
     *
     * @author Romane Bézier
     */
    private fun launchTimePicker() {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            presenter.getTime(hour, minute)
            presenter.saveAlarm(calendar.timeInMillis, mAlarmViewModel, viewLifecycleOwner)

        }
        TimePickerDialog(
            context, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), true
        ).show()
    }

    /**
     * Launch the time picker to update the alarm
     *
     * @param currentAlarm Alarm to update
     * @author Romane Bézier
     */
    override fun launchTimePickerUpdate(currentAlarm: Alarm) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            presenter.getTime(hour, minute)
            val updateAlarm = Alarm(currentAlarm.id, calendar.timeInMillis, currentAlarm.activate)
            presenter.updateAlarm(updateAlarm, mAlarmViewModel)
            if (currentAlarm.activate) {
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                initStopAlarm(alarmManager, currentAlarm)
                initStopAlert(alarmManager, currentAlarm)

                initStartAlarm(alarmManager, updateAlarm)
                initStartAlert(alarmManager, updateAlarm)
            }
        }
        TimePickerDialog(
            context, timePickerDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE
            ), true
        ).show()
    }

    /**
     * Convert the time to String
     *
     * @param milliSeconds Time to convert
     * @return Time in a String
     * @author Romane Bézier
     */
    override fun convertTime(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("HH:mm")

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    /**
     * Start the alarm
     *
     * @param currentAlarm Alarm to start
     * @author Romane Bézier
     */
    override fun startAlarm(currentAlarm: Alarm) {
        val updateAlarm = Alarm(currentAlarm.id, currentAlarm.time, true)
        presenter.updateAlarm(updateAlarm, mAlarmViewModel)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initStartAlarm(alarmManager, updateAlarm)
        initStartAlert(alarmManager, updateAlarm)
    }

    /**
     * Snooze the alarm
     *
     * @param currentAlarm Alarm to snooze
     * @author Romane Bézier
     */
    override fun snoozeAlarm(currentAlarm: Alarm) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        initStopAlarm(alarmManager, currentAlarm)
        initStopAlert(alarmManager, currentAlarm)

        val snoozeIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val snoozeBundle = Bundle()
        snoozeBundle.putParcelable("alarm", currentAlarm)
        snoozeIntent.putExtra("ALARM", snoozeBundle)
        presenter.snoozeAlarm(alarmManager, snoozeIntent, requireContext(), currentAlarm)
    }

    /**
     * Stop the alarm
     *
     * @param currentAlarm Alarm to stop
     * @author Romane Bézier
     */
    override fun stopAlarm(currentAlarm: Alarm) {
        val updateAlarm = Alarm(currentAlarm.id, currentAlarm.time, false)
        presenter.updateAlarm(updateAlarm, mAlarmViewModel)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initStopAlarm(alarmManager, currentAlarm)
        initStopAlert(alarmManager, currentAlarm)
    }

    /**
     * Delete the alarm
     *
     * @param currentAlarm Alarm to delete
     * @author Romane Bézier
     */
    override fun deleteAlarm(currentAlarm: Alarm) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            presenter.deleteAlarm(mAlarmViewModel, currentAlarm)
            if (currentAlarm.activate) {
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                initStopAlarm(alarmManager, currentAlarm)
                initStopAlert(alarmManager, currentAlarm)
            }
        }
        builder.setNegativeButton("No") { _, _ ->}
        builder.setTitle("Delete the alarm of ${convertTime(currentAlarm.time)}?")
        builder.setMessage("Are you sure you want to delete this alarm?")
        builder.create().show()
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
     * Prepare the starting of the alarm
     *
     * @param alarmManager Alarm manager for the alarms
     * @param currentAlarm Alarm to start
     * @author Romane Bézier
     */
    private fun initStartAlarm(alarmManager: AlarmManager, currentAlarm: Alarm) {
        val intentAlarm = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putParcelable("alarm", currentAlarm)
        intentAlarm.putExtra("ALARM", bundle)
        presenter.startAlarm(alarmManager, intentAlarm, requireContext(), currentAlarm)
    }

    /**
     * Prepare the starting of the lert
     *
     * @param alarmManager Alarm manager for the alarms
     * @param currentAlarm Alarm to start
     * @author Romane Bézier
     */
    private fun initStartAlert(alarmManager: AlarmManager, currentAlarm: Alarm) {
        val intentAlert = Intent(context, AlertReceiver::class.java)
        val bundleAlert = Bundle()
        bundleAlert.putParcelable("alarm", currentAlarm)
        intentAlert.putExtra("ALARM", bundleAlert)
        presenter.startAlert(alarmManager, intentAlert, requireContext(), currentAlarm)
    }

    /**
     * Prepare the stop of the alarm
     *
     * @param alarmManager Alarm manager for the alarms
     * @param currentAlarm Alarm to stop
     * @author Romane Bézier
     */
    private fun initStopAlarm(alarmManager: AlarmManager, currentAlarm: Alarm) {
        val stopIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val stopBundle = Bundle()
        stopBundle.putParcelable("alarm", currentAlarm)
        stopIntent.putExtra("ALARM", stopBundle)
        presenter.stopAlarm(alarmManager, stopIntent, requireContext(), currentAlarm)
    }

    /**
     * Prepare the stop of the alert
     *
     * @param alarmManager Alarm manager for the alarms
     * @param currentAlarm Alarm to stop
     * @author Romane Bézier
     */
    private fun initStopAlert(alarmManager: AlarmManager, currentAlarm: Alarm) {
        val intentAlert = Intent(context, AlertReceiver::class.java)
        val bundleAlert = Bundle()
        bundleAlert.putParcelable("alarm", currentAlarm)
        intentAlert.putExtra("ALARM", bundleAlert)
        presenter.stopAlert(alarmManager, intentAlert, requireContext(), currentAlarm)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val item: String = parent!!.getItemAtPosition(position).toString()

        Toast.makeText(parent.context, "Selected: $item", Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}