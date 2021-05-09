package com.sleewell.sleewell.reveil.data

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.data.model.Alarm
import kotlinx.android.synthetic.main.custom_row.view.*
import kotlinx.android.synthetic.main.new_fragment_stat.view.*

class ListAdapter(private val view: AlarmContract.View): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var alarmList = emptyList<Alarm>()
    private lateinit var context: Context

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = alarmList[position]
        holder.itemView.textViewTime.text = view.convertTime(currentItem.time)
        holder.itemView.checkBoxTime.isChecked = currentItem.activate

        if (currentItem.label.compareTo("") == 0) {
            holder.itemView.textLabel.visibility = View.GONE
        } else {
            holder.itemView.textLabel.visibility = View.VISIBLE
            holder.itemView.textLabel.text = currentItem.label
        }

        var days = ""
        if (currentItem.days[0]) {
            days += "M "
        }
        if (currentItem.days[1]) {
            days += "T "
        }
        if (currentItem.days[2]) {
            days += "W "
        }
        if (currentItem.days[3]) {
            days += "Th "
        }
        if (currentItem.days[4]) {
            days += "F "
        }
        if (currentItem.days[5]) {
            days += "S "
        }
        if (currentItem.days[6]) {
            days += "Su "
        }
        if (days.compareTo("") == 0) {
            holder.itemView.textDays.visibility = View.GONE
        } else {
            holder.itemView.textDays.visibility = View.VISIBLE
            holder.itemView.textDays.text = days
        }

        holder.itemView.rowLayout.setOnClickListener {
            view.updateAlarm(currentItem)
        }
        holder.itemView.rowLayout.setOnLongClickListener {
            view.changeVisibilityHolder()
            holder.itemView.rowLayout.checkBoxAlarm.isChecked = true
            return@setOnLongClickListener true
        }
        holder.itemView.deleteButton.setOnClickListener {
            view.deleteAlarm(currentItem, false)
        }
        holder.itemView.checkBoxTime.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (currentItem.displayed) {
                    view.startAlarm(currentItem)
                }
                currentItem.displayed = false
            }
            else
                view.stopAlarm(currentItem)
        }
        holder.itemView.checkBoxAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked)
                view.checkCheckList()
        }
    }

    fun getAlarmList() : List<Alarm> {
        return alarmList
    }

    fun setData(alarm: List<Alarm>) {
        this.alarmList = alarm
        notifyDataSetChanged()
    }

}