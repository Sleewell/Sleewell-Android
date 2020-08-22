package com.sleewell.sleewell.nav.alarms

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmAdapter
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.AlarmReceiver
import com.sleewell.sleewell.reveil.Presenter.AlarmPresenter
import com.sleewell.sleewell.reveil.View.ReminderActivity
import kotlinx.android.synthetic.main.activity_alarm.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.ArrayList

class AlarmsFragment : Fragment(), AlarmContract.View {

    companion object {
        lateinit var instance: AlarmsFragment
            private set
    }

    private lateinit var dashboardViewModel: AlarmsViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoReminders: TextView
    private lateinit var alarmAdapter: AlarmAdapter

    private lateinit var presenter: AlarmContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        instance = this

        dashboardViewModel =
            ViewModelProviders.of(this).get(AlarmsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)
        val fabCreateReminder: FloatingActionButton = root.findViewById(R.id.fabCreateReminder)
        fabCreateReminder.setOnClickListener {
            val intent = Intent(context, ReminderActivity::class.java)
            startActivity(intent)
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
            alarmAdapter = AlarmAdapter(reminderList)
            recyclerView.adapter = alarmAdapter
        }
    }

    /**
     * Snooze the alarm
     *
     */
    override fun snoozeAlarm() {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        presenter.snoozeAlarm(alarmManager, intent, context!!)
    }

    /**
     * Cancel the alarm
     *
     */
    override fun cancelAlarm() {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
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