package com.sleewell.sleewell.new_mvp.menu.alarm.view

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sleewell.sleewell.R
import com.sleewell.sleewell.nav.alarms.AlarmsFragment
import com.sleewell.sleewell.reveil.AlarmAdapter
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.AlertReceiver
import com.sleewell.sleewell.reveil.Presenter.AlarmPresenter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmFragment : Fragment(), AlarmContract.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoReminders: TextView
    private lateinit var alarmAdapter: AlarmAdapter

    private lateinit var presenter: AlarmContract.Presenter

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        lateinit var instance: AlarmFragment
            private set
        var id = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * When view is created
     *
     * @param savedInstanceState Save of the instance state
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        instance = this

        val root = inflater.inflate(R.layout.new_fragment_alarm, container, false)
        val fabCreateReminder: FloatingActionButton = root.findViewById(R.id.fabCreateReminder)
        fabCreateReminder.setOnClickListener {
            val date = Calendar.getInstance()
            val hour = date.get(Calendar.HOUR_OF_DAY)
            val minute = date.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(context, { view, hourOfDay, minute ->
                val formatted: String = presenter.getTime(hourOfDay, minute)

                val alarmManager =
                    activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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

        recyclerView = root.findViewById(R.id.recyclerViewReminders)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        textViewNoReminders = root.findViewById(R.id.textViewNoReminders)
        setPresenter(AlarmPresenter(this))
        presenter.onViewCreated(
            this.activity!!.getSharedPreferences(
                "com.sleewell",
                Context.MODE_PRIVATE
            )
        )

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
            alarmAdapter = AlarmAdapter(reminderList)
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
        presenter.cancelAlarm(alarmManager, intent, context!!)
    }

    /**
     * Set the presenter of the view
     *
     * @param presenter The presenter
     */
    override fun setPresenter(presenter: AlarmContract.Presenter) {
        this.presenter = presenter
    }
}