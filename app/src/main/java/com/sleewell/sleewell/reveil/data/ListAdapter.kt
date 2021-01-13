package com.sleewell.sleewell.reveil.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.reveil.AlarmContract
import com.sleewell.sleewell.reveil.data.model.Alarm
import kotlinx.android.synthetic.main.custom_row.view.*

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

        holder.itemView.rowLayout.setOnClickListener {
            view.launchTimePickerUpdate(currentItem)
        }
        holder.itemView.deleteButton.setOnClickListener {
            view.deleteAlarm(currentItem)
        }
        holder.itemView.checkBoxTime.setOnCheckedChangeListener {
            _, isChecked ->
                if (isChecked)
                    view.startAlarm(currentItem)
                else
                    view.stopAlarm(currentItem)
        }
    }

    fun setData(alarm: List<Alarm>) {
        this.alarmList = alarm
        notifyDataSetChanged()
    }

}