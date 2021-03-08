package com.sleewell.sleewell.mvp.menu.alarm.view

import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var validateupdateAlarm: ImageView

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
            changeVisibilityLayoutsCreate()
        }

        val toolbarCreateAlarm : Toolbar = root.findViewById(R.id.toolbar_create_alarm)
        toolbarCreateAlarm.setNavigationOnClickListener {
            changeVisibilityLayoutsAlarm()
        }

        val toolbarModifyAlarm : Toolbar = root.findViewById(R.id.toolbar_modify_alarm)
        toolbarModifyAlarm.setNavigationOnClickListener {
            changeVisibilityLayoutsAlarm()
        }

        val calendar = Calendar.getInstance()
        val timePicker : TimePicker = root.findViewById(R.id.time_picker_create_alarm)
        timePicker.setIs24HourView(true)
        timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePicker.minute = calendar.get(Calendar.MINUTE)

        val validatesaveAlarm : ImageView = root.findViewById(R.id.validate_create_alarm)
        validateSaveAlarm(validatesaveAlarm, timePicker, calendar)

        validateupdateAlarm = root.findViewById(R.id.validate_modify_alarm)

        val spinnerCreateAlarm : Spinner = root.findViewById(R.id.spinner_create_alarm)
        spinnerCreateAlarm.onItemSelectedListener = this
        val spinnerAlarm : Spinner = root.findViewById(R.id.spinner_create_alarm)
        spinnerAlarm.onItemSelectedListener = this

        val sounds: MutableList<String> = ArrayList()
        sounds.add("First")
        sounds.add("Second")
        sounds.add("Third")
        val dataAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, sounds)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCreateAlarm.adapter = dataAdapter
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

    private fun validateSaveAlarm(validateAlarm: ImageView, timePicker: TimePicker, calendar: Calendar) {
        validateAlarm.setOnClickListener {
            changeVisibilityLayoutsAlarm()
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            presenter.getTime(timePicker.hour, timePicker.minute)
            presenter.saveAlarm(calendar.timeInMillis, mAlarmViewModel, viewLifecycleOwner, checkBox_create_vibrate.isChecked, editText_create_alarm.text.toString())
        }
    }

    /**
     * Update the alarm
     *
     * @param currentAlarm Alarm to update
     * @author Romane Bézier
     */
    override fun updateAlarm(currentAlarm: Alarm) {

        changeVisibilityLayoutsModify()
        //changer les valeurs: timepicker, vibrate, label
        //validate update Alarm

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentAlarm.time

        time_picker_modify_alarm.setIs24HourView(true)
        val hour = SimpleDateFormat("HH").format(calendar.time)
        val minute = SimpleDateFormat("mm").format(calendar.time)
        time_picker_modify_alarm.hour = hour.toInt()
        time_picker_modify_alarm.minute = minute.toInt()

        checkBox_modify_vibrate.isChecked = currentAlarm.vibrate
        val editable: Editable = SpannableStringBuilder(currentAlarm.label)
        editText_modify_alarm.text = editable

        validateupdateAlarm.setOnClickListener {
            changeVisibilityLayoutsAlarm()

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, time_picker_modify_alarm.hour)
            calendar.set(Calendar.MINUTE, time_picker_modify_alarm.minute)
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            presenter.getTime(time_picker_modify_alarm.hour, time_picker_modify_alarm.minute)
            val updateAlarm = Alarm(
                currentAlarm.id,
                calendar.timeInMillis,
                currentAlarm.activate,
                checkBox_modify_vibrate.isChecked,
                label_modify_alarm.text.toString()
            )
            presenter.updateAlarm(updateAlarm, mAlarmViewModel)
            if (currentAlarm.activate) {
                val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                initStopAlarm(alarmManager, currentAlarm)
                initStopAlert(alarmManager, currentAlarm)

                initStartAlarm(alarmManager, updateAlarm)
                initStartAlert(alarmManager, updateAlarm)
            }
        }
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

        val updateAlarm = Alarm(
            currentAlarm.id,
            currentAlarm.time,
            true,
            currentAlarm.vibrate,
            currentAlarm.label
        ) //ADD PARAMETERS
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
        val updateAlarm = Alarm(
            currentAlarm.id,
            currentAlarm.time,
            false,
            currentAlarm.vibrate,
            currentAlarm.label
        ) //ADD PARAMETERS
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
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /**
     * Change visibility of the layout alarm to create alarm
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsCreate() {
        toolbar_create_alarm.visibility = View.VISIBLE
        time_picker_create_alarm.visibility = View.VISIBLE
        repeat_create_text.visibility = View.VISIBLE
        daypicker_create_layout.visibility = View.VISIBLE
        create_alarm_ringtone.visibility = View.VISIBLE
        spinner_create_alarm.visibility = View.VISIBLE
        create_vibrate.visibility = View.VISIBLE
        checkBox_create_vibrate.visibility = View.VISIBLE
        label_create_alarm.visibility = View.VISIBLE
        editText_create_alarm.visibility = View.VISIBLE
        recyclerView_alarm.visibility = View.INVISIBLE
        add_alarm_button.hide()
        toolbar_modify_alarm.visibility = View.INVISIBLE
        time_picker_modify_alarm.visibility = View.INVISIBLE
        repeat_modify_text.visibility = View.INVISIBLE
        daypicker_modify_layout.visibility = View.INVISIBLE
        modify_alarm_ringtone.visibility = View.INVISIBLE
        spinner_modify_alarm.visibility = View.INVISIBLE
        modify_vibrate.visibility = View.INVISIBLE
        checkBox_modify_vibrate.visibility = View.INVISIBLE
        label_modify_alarm.visibility = View.INVISIBLE
        editText_modify_alarm.visibility = View.INVISIBLE
    }

    /**
     * Change visibility of the layout alarm to modify alarm
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsModify() {
        toolbar_create_alarm.visibility = View.INVISIBLE
        time_picker_create_alarm.visibility = View.INVISIBLE
        repeat_create_text.visibility = View.INVISIBLE
        daypicker_create_layout.visibility = View.INVISIBLE
        create_alarm_ringtone.visibility = View.INVISIBLE
        spinner_create_alarm.visibility = View.INVISIBLE
        create_vibrate.visibility = View.INVISIBLE
        checkBox_create_vibrate.visibility = View.INVISIBLE
        label_create_alarm.visibility = View.INVISIBLE
        editText_create_alarm.visibility = View.INVISIBLE
        recyclerView_alarm.visibility = View.INVISIBLE
        add_alarm_button.hide()
        toolbar_modify_alarm.visibility = View.VISIBLE
        time_picker_modify_alarm.visibility = View.VISIBLE
        repeat_modify_text.visibility = View.VISIBLE
        daypicker_modify_layout.visibility = View.VISIBLE
        modify_alarm_ringtone.visibility = View.VISIBLE
        spinner_modify_alarm.visibility = View.VISIBLE
        modify_vibrate.visibility = View.VISIBLE
        checkBox_modify_vibrate.visibility = View.VISIBLE
        label_modify_alarm.visibility = View.VISIBLE
        editText_modify_alarm.visibility = View.VISIBLE
    }

    /**
     * Change visibility of the layout alarm to show alarm
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsAlarm() {
        toolbar_create_alarm.visibility = View.INVISIBLE
        time_picker_create_alarm.visibility = View.INVISIBLE
        repeat_create_text.visibility = View.INVISIBLE
        daypicker_create_layout.visibility = View.INVISIBLE
        create_alarm_ringtone.visibility = View.INVISIBLE
        spinner_create_alarm.visibility = View.INVISIBLE
        create_vibrate.visibility = View.INVISIBLE
        checkBox_create_vibrate.visibility = View.INVISIBLE
        label_create_alarm.visibility = View.INVISIBLE
        editText_create_alarm.visibility = View.INVISIBLE
        recyclerView_alarm.visibility = View.VISIBLE
        add_alarm_button.show()
        toolbar_modify_alarm.visibility = View.INVISIBLE
        time_picker_modify_alarm.visibility = View.INVISIBLE
        repeat_modify_text.visibility = View.INVISIBLE
        daypicker_modify_layout.visibility = View.INVISIBLE
        modify_alarm_ringtone.visibility = View.INVISIBLE
        spinner_modify_alarm.visibility = View.INVISIBLE
        modify_vibrate.visibility = View.INVISIBLE
        checkBox_modify_vibrate.visibility = View.INVISIBLE
        label_modify_alarm.visibility = View.INVISIBLE
        editText_modify_alarm.visibility = View.INVISIBLE
    }
}