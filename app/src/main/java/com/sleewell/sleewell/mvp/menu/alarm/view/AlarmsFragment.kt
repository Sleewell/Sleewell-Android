package com.sleewell.sleewell.mvp.menu.alarm.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kevalpatel.ringtonepicker.RingtonePickerDialog
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.AlertReceiver
import com.sleewell.sleewell.reveil.data.ListAdapter
import com.sleewell.sleewell.reveil.data.model.Alarm
import com.sleewell.sleewell.reveil.data.viewmodel.AlarmViewModel
import com.sleewell.sleewell.reveil.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.daypicker_layout.view.*
import kotlinx.android.synthetic.main.new_fragment_alarm.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AlarmsFragment : Fragment(), AlarmContract.View, AdapterView.OnItemSelectedListener {

    private lateinit var mAlarmViewModel: AlarmViewModel
    private lateinit var presenter: AlarmContract.Presenter
    private lateinit var buttonValidateUpdateAlarm: Button
    private lateinit var buttonValidateSaveAlarm: Button
    private lateinit var ringtone: Uri

    private lateinit var layoutCreate: ConstraintLayout
    private lateinit var layoutModify: ConstraintLayout
    private lateinit var layoutHome: ConstraintLayout

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: AlarmsFragment
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view. This value may be null.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        instance = this
        val root = inflater.inflate(R.layout.new_fragment_alarm, container, false)

        setHasOptionsMenu(true)

        ringtone = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
        buttonValidateSaveAlarm = root.findViewById(R.id.button_validate_create)
        buttonValidateUpdateAlarm = root.findViewById(R.id.button_validate_modify)
        layoutCreate = root.findViewById(R.id.layout_create_alarm)
        layoutModify = root.findViewById(R.id.layout_modify_alarm)
        layoutHome = root.findViewById(R.id.layout_alarm)

        val floatingActionButton: FloatingActionButton = root.findViewById(R.id.add_alarm_button)
        floatingActionButton.setOnClickListener {
            changeVisibilityLayoutsCreate()
            resetDays()
            createAlarm(root)
        }

        val toolbarCreateAlarm: Toolbar = root.findViewById(R.id.toolbar_create_alarm)
        toolbarCreateAlarm.setNavigationOnClickListener {
            changeVisibilityLayoutsAlarm()
        }

        val toolbarModifyAlarm: Toolbar = root.findViewById(R.id.toolbar_modify_alarm)
        toolbarModifyAlarm.setNavigationOnClickListener {
            changeVisibilityLayoutsAlarm()
        }

        val checkBoxAllAlarms: CheckBox = root.findViewById(R.id.checkBoxAllAlarms)
        checkBoxAllAlarms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                checkAllAlarms()
            else {
                uncheckAllAlarms()
            }
        }

        val alarms: MutableList<Uri> = ArrayList()
        val ringtoneMgr = RingtoneManager(activity)
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM)
        val alarmsCursor = ringtoneMgr.cursor
        val alarmsCount = alarmsCursor.count
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            alarmsCursor.close()
            return null
        }
        while (!alarmsCursor.isAfterLast && alarmsCursor.moveToNext()) {
            val currentPosition = alarmsCursor.position
            alarms.add(ringtoneMgr.getRingtoneUri(currentPosition))
        }

        val spinnerCreateAlarm: Button = root.findViewById(R.id.spinner_create_alarm)
        spinnerCreateAlarm.setOnClickListener {
            val ringtonePickerBuilder = RingtonePickerDialog.Builder(
                context!!,
                parentFragmentManager
            )
            ringtonePickerBuilder.setTitle("Select ringtone")
            ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM)
            ringtonePickerBuilder.setPositiveButtonText("SET RINGTONE")
            ringtonePickerBuilder.setCancelButtonText("CANCEL")
            ringtonePickerBuilder.setPlaySampleWhileSelection(true)
            ringtonePickerBuilder.setCurrentRingtoneUri(alarms[0])
            ringtonePickerBuilder.setListener { ringtoneName, ringtoneUri ->
                spinnerCreateAlarm.text = ringtoneName
                ringtone = ringtoneUri
            }
            ringtonePickerBuilder.show()
        }

        val spinnerModifyAlarm: Button = root.findViewById(R.id.spinner_modify_alarm)
        spinnerModifyAlarm.setOnClickListener {
            val ringtonePickerBuilder = RingtonePickerDialog.Builder(
                context!!,
                parentFragmentManager
            )
            ringtonePickerBuilder.setTitle("Select ringtone")
            ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM)
            ringtonePickerBuilder.setPositiveButtonText("SET RINGTONE")
            ringtonePickerBuilder.setCancelButtonText("CANCEL")
            ringtonePickerBuilder.setPlaySampleWhileSelection(true)
            val defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
                activity!!.applicationContext, RingtoneManager.TYPE_RINGTONE
            )
            ringtonePickerBuilder.setCurrentRingtoneUri(defaultRingtoneUri)
            ringtonePickerBuilder.setListener { ringtoneName, ringtoneUri ->
                spinnerModifyAlarm.text = ringtoneName
                ringtone = ringtoneUri
            }
            ringtonePickerBuilder.show()
        }

        val adapter = ListAdapter(this)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView_alarm)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        divider.setDrawable(context!!.resources.getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divider)

        val deleteSelectedButton: ImageView = root.findViewById(R.id.deleteSelectedButton)
        deleteSelectedButton.setOnClickListener {
            deleteSelectedAlarms(adapter)
        }

        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmViewModel.readAllData.observe(viewLifecycleOwner, { alarm ->
            adapter.setData(alarm)
        })

        setPresenter(AlarmPresenter(this))
        return root
    }

    /**
     * Reset values of Day picker
     *
     * @author Romane Bézier
     */
    private fun resetDays() {
        daypicker_create_layout.tM.isChecked = false
        daypicker_create_layout.tT.isChecked = false
        daypicker_create_layout.tW.isChecked = false
        daypicker_create_layout.tTh.isChecked = false
        daypicker_create_layout.tF.isChecked = false
        daypicker_create_layout.tS.isChecked = false
        daypicker_create_layout.tSu.isChecked = false
    }

    /**
     * Set the alarm with specifics days.
     *
     * @param dayOfWeek Day of the week the alarm need to be create.
     * @param calendar Calendar use to create the alarm.
     * @param timePicker Time picker use to create the alarm.
     * @param days Days the alarm need to be activated.
     * @param index Index of the alarm.
     * @author Romane Bézier
     */
    private fun setAlarmWithDay(
        dayOfWeek: Int,
        calendar: Calendar,
        timePicker: TimePicker,
        days: List<Boolean>,
        index: Int
    ) {
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)
        var copy = calendar
        if (calendar.before(Calendar.getInstance())) {
            copy.add(Calendar.DATE, 7)
        }
        var show = true
        if (index > 0)
            show = false
        presenter.getTime(timePicker.hour, timePicker.minute)
        presenter.saveAlarm(
            copy.timeInMillis,
            mAlarmViewModel,
            viewLifecycleOwner,
            days,
            ringtone,
            checkBox_create_vibrate.isChecked,
            editText_create_alarm.text.toString(),
            index,
            false,
            show
        )
    }

    /**
     * Set the alarm without specific days.
     *
     * @param calendar Calendar use to create the alarm.
     * @param timePicker Time picker use to create the alarm.
     * @param days Days the alarm need to be activated.
     * @author Romane Bézier
     */
    private fun setAlarmWithoutDay(
        calendar: Calendar,
        timePicker: TimePicker,
        days: List<Boolean>,
    ) {
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        presenter.getTime(timePicker.hour, timePicker.minute)
        presenter.saveAlarm(
            calendar.timeInMillis,
            mAlarmViewModel,
            viewLifecycleOwner,
            days,
            ringtone,
            checkBox_create_vibrate.isChecked,
            editText_create_alarm.text.toString(),
            0,
            false,
            true
        )
    }

    /**
     * Validate the save of the alarm.
     *
     * @param calendar Calendar use to save the alarm.
     * @param timePicker Time picker use to save the alarm.
     * @author Romane Bézier
     */
    private fun funcValidateSaveAlarm(calendar: Calendar, timePicker: TimePicker) {

        changeVisibilityLayoutsAlarm()

        val days = listOf(
            daypicker_create_layout.tM.isChecked,
            daypicker_create_layout.tT.isChecked,
            daypicker_create_layout.tW.isChecked,
            daypicker_create_layout.tTh.isChecked,
            daypicker_create_layout.tF.isChecked,
            daypicker_create_layout.tS.isChecked,
            daypicker_create_layout.tSu.isChecked
        )

        if (days.contains(true)) {
            var i = 0
            var index = 0
            while (i < days.size) {
                if (days[i] && i < 6) {
                    setAlarmWithDay(i + 2, calendar, timePicker, days, index)
                    index++
                } else if (days[i] && i == 6) {
                    setAlarmWithDay(1, calendar, timePicker, days, index)
                    index++
                }
                i++
            }
        } else {
            setAlarmWithoutDay(calendar, timePicker, days)
        }
    }

    /**
     * Create the alarm.
     *
     * @param root The view of the alarm.
     * @author Romane Bézier
     */
    private fun createAlarm(root: View) {
        val calendar = Calendar.getInstance()
        val timePicker: TimePicker = root.findViewById(R.id.time_picker_create_alarm)
        timePicker.setIs24HourView(true)
        timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePicker.minute = calendar.get(Calendar.MINUTE)

        buttonValidateSaveAlarm.setOnClickListener {
            funcValidateSaveAlarm(calendar, timePicker)
        }
    }

    /**
     * Switch all the check box of the alarms to true.
     *
     * @author Romane Bézier
     */
    private fun checkAllAlarms() {
        val childCount = recyclerView_alarm.childCount
        var i = 0
        while (i < childCount) {
            val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
            holder.itemView.rowLayout.checkBoxAlarm.isChecked = true
            i++
        }
    }

    /**
     * Switch all the check box of the alarms to false.
     *
     * @author Romane Bézier
     */
    private fun uncheckAllAlarms() {
        val childCount = recyclerView_alarm.childCount
        var i = 0
        while (i < childCount) {
            val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
            holder.itemView.rowLayout.checkBoxAlarm.isChecked = false
            i++
        }
    }

    /**
     * Delete all the alarms selected.
     *
     * @param adapter Adapter of the recycler view of the alarm.
     */
    private fun deleteSelectedAlarms(adapter: ListAdapter) {
        val alarmList = adapter.getAlarmList()
        val childCount = recyclerView_alarm.childCount
        var i = 0
        while (i < childCount) {
            val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
            if (holder.itemView.rowLayout.checkBoxAlarm.isChecked) {
                holder.itemView.rowLayout.checkBoxAlarm.isChecked = false
                deleteAlarm(alarmList[i], true)
            }
            i++
        }
    }

    /**
     * Validate update of the alarm.
     *
     * @param currentAlarm Current alarm that is updated.
     * @author Romane Bézier
     */
    private fun funcValidateUpdateAlarm(currentAlarm: Alarm) {
        changeVisibilityLayoutsAlarm()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, time_picker_modify_alarm.hour)
        calendar.set(Calendar.MINUTE, time_picker_modify_alarm.minute)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        presenter.getTime(time_picker_modify_alarm.hour, time_picker_modify_alarm.minute)

        val days = listOf(
            daypicker_modify_layout.tM.isChecked,
            daypicker_modify_layout.tT.isChecked,
            daypicker_modify_layout.tW.isChecked,
            daypicker_modify_layout.tTh.isChecked,
            daypicker_modify_layout.tF.isChecked,
            daypicker_modify_layout.tS.isChecked,
            daypicker_modify_layout.tSu.isChecked
        )

        val updateAlarm = Alarm(
            currentAlarm.id,
            calendar.timeInMillis,
            currentAlarm.activate,
            days,
            ringtone.toString(),
            checkBox_modify_vibrate.isChecked,
            editText_modify_alarm.text.toString(),
            true,
            currentAlarm.show
        )

        presenter.updateAlarm(updateAlarm, mAlarmViewModel)

        if (currentAlarm.activate) {
            val alarmManager =
                requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            initStopAlarm(alarmManager, currentAlarm)
            initStopAlert(alarmManager, currentAlarm)

            initStartAlarm(alarmManager, updateAlarm, false)
            initStartAlert(alarmManager, updateAlarm)
        }
    }

    /**
     * Update the alarm.
     *
     * @param currentAlarm Current alarm that is updated.
     * @author Romane Bézier
     */
    @SuppressLint("SimpleDateFormat")
    override fun updateAlarm(currentAlarm: Alarm) {

        changeVisibilityLayoutsModify()
        spinner_modify_alarm.text =
            RingtoneManager.getRingtone(context, Uri.parse(currentAlarm.ringtone)).getTitle(context)

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

        daypicker_modify_layout.tM.isChecked = currentAlarm.days[0]
        daypicker_modify_layout.tT.isChecked = currentAlarm.days[1]
        daypicker_modify_layout.tW.isChecked = currentAlarm.days[2]
        daypicker_modify_layout.tTh.isChecked = currentAlarm.days[3]
        daypicker_modify_layout.tF.isChecked = currentAlarm.days[4]
        daypicker_modify_layout.tS.isChecked = currentAlarm.days[5]
        daypicker_modify_layout.tSu.isChecked = currentAlarm.days[6]

        buttonValidateUpdateAlarm.setOnClickListener {
            funcValidateUpdateAlarm(currentAlarm)
        }
    }

    /**
     * Convert the time to String.
     *
     * @param milliSeconds Time to convert.
     * @return Time in a String.
     * @author Romane Bézier
     */
    @SuppressLint("SimpleDateFormat")
    override fun convertTime(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("HH:mm")

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    /**
     * Start the alarm.
     *
     * @param currentAlarm Alarm to start.
     * @author Romane Bézier
     */
    override fun startAlarm(currentAlarm: Alarm, restart: Boolean) {

        val updateAlarm = Alarm(
            currentAlarm.id,
            currentAlarm.time,
            true,
            currentAlarm.days,
            currentAlarm.ringtone,
            currentAlarm.vibrate,
            currentAlarm.label,
            false,
            currentAlarm.show
        )
        presenter.updateAlarm(updateAlarm, mAlarmViewModel)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initStartAlarm(alarmManager, updateAlarm, restart)
        initStartAlert(alarmManager, updateAlarm)
    }

    /**
     * Snooze the alarm.
     *
     * @param currentAlarm Alarm to snooze.
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
     * Stop the alarm.
     *
     * @param currentAlarm Alarm to stop.
     * @author Romane Bézier
     */
    override fun stopAlarm(currentAlarm: Alarm) {
        val updateAlarm = Alarm(
            currentAlarm.id,
            currentAlarm.time,
            false,
            currentAlarm.days,
            currentAlarm.ringtone,
            currentAlarm.vibrate,
            currentAlarm.label,
            true,
            currentAlarm.show
        )
        presenter.updateAlarm(updateAlarm, mAlarmViewModel)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initStopAlarm(alarmManager, currentAlarm)
        initStopAlert(alarmManager, currentAlarm)
    }

    /**
     * Delete the alarm.
     *
     * @param currentAlarm Alarm to delete.
     * @param selectedAlarm Selected the alarm are delete.
     * @author Romane Bézier
     */
    override fun deleteAlarm(currentAlarm: Alarm, selectedAlarm: Boolean) {
        if (!selectedAlarm) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(R.string.yes) { _, _ ->
                presenter.deleteAlarm(mAlarmViewModel, currentAlarm)
                if (currentAlarm.activate) {
                    val alarmManager =
                        requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    initStopAlarm(alarmManager, currentAlarm)
                    initStopAlert(alarmManager, currentAlarm)
                }
            }
            builder.setNegativeButton(R.string.no) { _, _ -> }
            builder.setTitle("${R.string.deleteAlarmTitle} ${convertTime(currentAlarm.time)}?")
            builder.setMessage(R.string.deleteAlarmMessage)
            builder.create().show()
        } else {
            presenter.deleteAlarm(mAlarmViewModel, currentAlarm)
            if (currentAlarm.activate) {
                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                initStopAlarm(alarmManager, currentAlarm)
                initStopAlert(alarmManager, currentAlarm)
            }
        }
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
     * Prepare the starting of the alarm.
     *
     * @param alarmManager Alarm manager for the alarms.
     * @param currentAlarm Alarm to start.
     * @author Romane Bézier
     */
    private fun initStartAlarm(alarmManager: AlarmManager, currentAlarm: Alarm, restart: Boolean) {
        val intentAlarm = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putParcelable("alarm", currentAlarm)
        intentAlarm.putExtra("ALARM", bundle)
        presenter.startAlarm(alarmManager, intentAlarm, requireContext(), currentAlarm, restart)
    }

    /**
     * Prepare the starting of the alert.
     *
     * @param alarmManager Alarm manager for the alarms.
     * @param currentAlarm Alarm to start.
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
     * Prepare the stop of the alarm.
     *
     * @param alarmManager Alarm manager for the alarms.
     * @param currentAlarm Alarm to stop.
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
     * Prepare the stop of the alert.
     *
     * @param alarmManager Alarm manager for the alarms.
     * @param currentAlarm Alarm to stop.
     * @author Romane Bézier
     */
    private fun initStopAlert(alarmManager: AlarmManager, currentAlarm: Alarm) {
        val intentAlert = Intent(context, AlertReceiver::class.java)
        val bundleAlert = Bundle()
        bundleAlert.putParcelable("alarm", currentAlarm)
        intentAlert.putExtra("ALARM", bundleAlert)
        presenter.stopAlert(alarmManager, intentAlert, requireContext(), currentAlarm)
    }

    /**
     * Callback method to be invoked when an item in this view has been selected.
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    /**
     * Callback method to be invoked when the selection disappears from this view.
     *
     * @param p0 The AdapterView that now contains no selected item.
     */
    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    /**
     * Change visibility of the holder.
     *
     * @author Romane Bézier
     */
    override fun changeVisibilityHolder() {
        checkBoxAllAlarms.visibility = View.VISIBLE
        deleteSelectedButton.visibility = View.VISIBLE

        val childCount = recyclerView_alarm.childCount
        var i = 0
        while (i < childCount) {
            val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
            holder.itemView.rowLayout.checkBoxAlarm.visibility = View.VISIBLE
            holder.itemView.rowLayout.deleteButton.visibility = View.INVISIBLE
            holder.itemView.rowLayout.checkBoxTime.visibility = View.INVISIBLE
            i++
        }
    }

    /**
     * Check if the check boxed are not check.
     *
     * @author Romane Bézier
     */
    override fun checkCheckList() {
        val childCount = recyclerView_alarm.childCount
        var i = 0
        var check = false
        while (i < childCount) {
            val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
            if (holder.itemView.rowLayout.checkBoxAlarm.isChecked) {
                check = true
            }
            i++
        }
        if (!check) {
            checkBoxAllAlarms.visibility = View.INVISIBLE
            deleteSelectedButton.visibility = View.INVISIBLE
            i = 0
            while (i < childCount) {
                val holder = recyclerView_alarm.getChildViewHolder(recyclerView_alarm.getChildAt(i))
                holder.itemView.rowLayout.checkBoxAlarm.visibility = View.INVISIBLE
                holder.itemView.rowLayout.deleteButton.visibility = View.VISIBLE
                holder.itemView.rowLayout.checkBoxTime.visibility = View.VISIBLE
                i++
            }
        }
    }

    /**
     * Change visibility of the layout alarm to create alarm.
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsCreate() {
        layoutHome.visibility = View.GONE
        layoutModify.visibility = View.GONE
        layoutCreate.visibility = View.VISIBLE
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
        button_validate_create.visibility = View.VISIBLE
        recyclerView_alarm.visibility = View.INVISIBLE
        toolbar_alarm.visibility = View.INVISIBLE
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
        button_validate_modify.visibility = View.INVISIBLE
        editText_create_alarm.text.clear()
        checkBox_create_vibrate.isChecked = true

        val title = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
        ).getTitle(context)
        spinner_create_alarm.text = title
    }

    /**
     * Change visibility of the layout alarm to modify alarm.
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsModify() {
        layoutHome.visibility = View.GONE
        layoutCreate.visibility = View.GONE
        layoutModify.visibility = View.VISIBLE
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
        button_validate_create.visibility = View.INVISIBLE
        recyclerView_alarm.visibility = View.INVISIBLE
        toolbar_alarm.visibility = View.INVISIBLE
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
        button_validate_modify.visibility = View.VISIBLE
    }

    /**
     * Change visibility of the layout alarm to show alarm.
     *
     * @author Romane Bézier
     */
    private fun changeVisibilityLayoutsAlarm() {
        layoutModify.visibility = View.GONE
        layoutCreate.visibility = View.GONE
        layoutHome.visibility = View.VISIBLE
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
        toolbar_alarm.visibility = View.VISIBLE
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